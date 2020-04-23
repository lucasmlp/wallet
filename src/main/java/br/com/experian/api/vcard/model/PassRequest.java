package br.com.experian.api.vcard.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PassRequest {

	private String firstName;
	private String lastName;
	private String uuid;
	private String uniqueID;
	private String imageURL;

	private String name;
	private String title;
	private String email;
	private List<Phone> phones = new ArrayList<Phone>();
	private String vcard = null;

	public PassRequest(String name) {
		this.name = name;
	}

	public PassRequest(String firstName, String lastName, String uuid, String uniqueID, String imageURL) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.uuid = uuid;
		this.uniqueID = uniqueID;
		this.imageURL = imageURL;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public String getVcard() {
		return vcard;
	}

	public void setVcard(String vcard) {
		this.vcard = vcard;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PassRequest that = (PassRequest) o;
		return Objects.equals(uniqueID, that.uniqueID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uniqueID);
	}
}
