
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {

	private String	name;
	private String	surnames;
	private String	email;
	private String	phoneNumber;
	private String	address;
	private String	gender;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurnames() {
		return this.surnames;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?[0-9]{9,}$")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	@Pattern(regexp = "^(MALE|FEMALE|OTHER)$")
	public String getGender() {
		return this.gender;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSurnames(final String surnames) {
		this.surnames = surnames;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}


	// Relationships ----------------------------------------------------------------------------------

	private UserAccount	userAccount;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

}
