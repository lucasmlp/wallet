package br.com.experian.api.vcard.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.experian.api.vcard.service.ApplePassServiceImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.experian.api.vcard.model.PassRequest;

@RestController
	@RequestMapping("/passkit")
public class PassController {

	@Autowired
	private ApplePassServiceImpl applePassServiceImpl;

	/** The Constant PASSKIT_CONTENT_TYPE. */
	private static final String PASSKIT_CONTENT_TYPE = "application/vnd.apple.pkpass";

	/**
	 * Gets the pkpass file for the User
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void getPasskit(HttpServletRequest request, HttpServletResponse response) {

		// Code to get the User from UUID which is retrived from request or by any other
		// means
		PassRequest passRequest = new PassRequest("Lucas", "Abreu", "123", "12", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.facebook.com%2Fpages%2FImagens-tumblr%2F1316298318394242&psig=AOvVaw3Grw_kHBmGcUENZ0QQGi2s&ust=1586011687139000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJCAxI_AzOgCFQAAAAAdAAAAABAD");

		byte[] pkpassFile = applePassServiceImpl.createPass(passRequest);

		// Prepare response to start download
		response.setStatus(200);
		response.setContentLength(pkpassFile.length);
		response.setContentType(PASSKIT_CONTENT_TYPE);
		response.setHeader("Content-Disposition", "inline; filename=\"" + applePassServiceImpl.getFileName(passRequest) + "\"");

		ByteArrayInputStream bais = new ByteArrayInputStream(pkpassFile);
		try {
			IOUtils.copy(bais, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			// log the exception.
		}

	}
}
