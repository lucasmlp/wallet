package br.com.experian.api.vcard.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.experian.api.vcard.model.PassRequest;

import de.brendamour.jpasskit.PKBarcode;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.enums.PKBarcodeFormat;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.signing.PKFileBasedSigningUtil;
import de.brendamour.jpasskit.signing.PKPassTemplateInMemory;
import de.brendamour.jpasskit.signing.PKSigningException;
import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningInformationUtil;

@Service
public class ApplePassServiceImpl {

	/** The foreground color of pass */
    private final String foregroundColor = "rgb(255,255,255)";
    
    /** The background color of pass */
    private final String backgroundColor = "rgb(207,18,45)";
    
    /** The pass type identifier, taken from the certificate */
    private final String passTypeIdentifier = "pass.com.example.id";
    
    /** The team ID, taken from the certificate */
    private final String teamID = "YOUR_TEAM_ID";
    
    /** version */
    private final int version = 1;
    
    /** The pass description */
    private final String description = "Example card";
    
    /** The pass type identifier, taken from the certificate */
    private final String logoIconFileName = "logo.png";
    
    /** The name of primary field of the pass */
    private final String primaryFieldName = "firstName";
    
    /** The name of primary header of the pass */
    private final String primaryHeader = "Name";
    
    /** The name of secondary field of the pass */
    private final String secondaryFieldName = "ID Number";
    
    /** The name of secondary header of the pass */
    private final String secondaryHeader = "Unique ID";

    /** The name of secondary field of the pass */
    private final String auxiliaryFieldName = "Phones";

    /** The name of secondary header of the pass */
    private final String auxiliaryHeader = "Phones";
    
    /** The name of organization issuing the pass */
    private final String orgName = "YOUR_ORG_NAME";
    
    /** The Pass certificate path */
    private final String passCertificateFileName = "Wallet.p12";
    
    /** The password for pass certificate */
    private final String password = "PASS_CERTIFICATE_PASSWORD";
    
    /** The Apple developer certificate path */
    private final String developerCertificateFileName = "AppleWWDRCA.cer";
    
    /**
     * Create a .pkpass file for User
     *
     * @param passRequest the user
     * @return byte array representing the zipped archive of apple passkit
     */
     
     public byte[] createPass(PassRequest passRequest, String clientId) {

        String fullName = passRequest.getName();
        String email = passRequest.getEmail();
        String phones = passRequest.getPhones().toArray()[0] + " " +passRequest.getPhones().toArray()[1];

        PKPass pass = new PKPass();
        PKGenericPass gp = new PKGenericPass();

        PKField primaryField = new PKField(primaryFieldName, primaryHeader, fullName);
        PKField secondaryField = new PKField(secondaryFieldName, secondaryHeader, email);
        PKField auxiliaryField = new PKField(auxiliaryFieldName, auxiliaryHeader, phones);
        gp.addPrimaryField(primaryField);
        gp.addSecondaryField(secondaryField);
        gp.addAuxiliaryField(auxiliaryField);
        
        PKBarcode barcode = new PKBarcode();
        barcode.setFormat(PKBarcodeFormat.PKBarcodeFormatQR);
//        barcode.setMessage(passRequest.getUuid().toString());
//        barcode.setMessageEncoding(Charset.forName("utf-8"));
        List<PKBarcode> barcodeList = new ArrayList<PKBarcode>();
        barcodeList.add(barcode);
        
        pass.setBackgroundColor(backgroundColor);
        pass.setFormatVersion(version);
        pass.setPassTypeIdentifier(passTypeIdentifier);
        pass.setSerialNumber(passRequest.getUniqueID().toString());
        pass.setTeamIdentifier(teamID);
        pass.setOrganizationName(orgName);
        pass.setDescription(description);
        pass.setForegroundColor(foregroundColor);
        pass.setGeneric(gp);
        pass.setBarcodes(barcodeList);
        
        PKPassTemplateInMemory pkPassTemplateInMemory = new PKPassTemplateInMemory();
        PKSigningInformation pkSigningInformation;
        try {
                ClassLoader classLoader =   Thread.currentThread().getContextClassLoader();
                InputStream logoFile =   classLoader.getResourceAsStream(logoIconFileName);
                InputStream iconFile = classLoader.getResourceAsStream(logoIconFileName);
                URL url = new URL(passRequest.getImageURL());
                
                pkPassTemplateInMemory.addFile(PKPassTemplateInMemory.PK_ICON, iconFile);
                pkPassTemplateInMemory.addFile(PKPassTemplateInMemory.PK_ICON_RETINA, iconFile);
                pkPassTemplateInMemory.addFile(PKPassTemplateInMemory.PK_LOGO, logoFile);
                pkPassTemplateInMemory.addFile(PKPassTemplateInMemory.PK_LOGO_RETINA, logoFile);
                pkPassTemplateInMemory.addFile(PKPassTemplateInMemory.PK_THUMBNAIL, url);
                pkPassTemplateInMemory.addFile(PKPassTemplateInMemory.PK_THUMBNAIL_RETINA, url);
                
                pkSigningInformation = new PKSigningInformationUtil()
                    .loadSigningInformationFromPKCS12AndIntermediateCertificate(
                            classLoader.getResourceAsStream(passCertificateFileName), password,
                            classLoader.getResourceAsStream(developerCertificateFileName));
                            
                PKFileBasedSigningUtil pkSigningUtil = new PKFileBasedSigningUtil();
                byte[] signedAndZippedPkPassArchive = pkSigningUtil.createSignedAndZippedPkPassArchive(pass,
                    pkPassTemplateInMemory, pkSigningInformation);
            
                return signedAndZippedPkPassArchive;
            } catch (CertificateException e1) {
                // log the exception.
            } catch (IOException e1) {
                // log the exception.
            } catch (PKSigningException e) {
                // log the exception.
            }
		return null;
     }
     
    /**
     * Returns the name of the pkpass file
     *
     * @return filename for pkpass file
     */
    
    public String getFileName(PassRequest user) {
        return "user_id_card_" + user.getFirstName() + "_" + user.getLastName() + ".pkpass";
    }
}
