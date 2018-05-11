
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Store extends DomainEntity {

	private String	name;
	private String	logo;
	private String	phoneNumber;
	private String	email;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@URL
	public String getLogo() {
		return this.logo;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?[0-9]{9,}$")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setLogo(final String logo) {
		this.logo = logo;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

}
