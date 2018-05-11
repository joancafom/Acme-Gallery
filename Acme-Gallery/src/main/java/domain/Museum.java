
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Museum extends DomainEntity {

	private String			name;
	private String			address;
	private GPSCoordinates	coordinates;
	private String			email;
	private String			phoneNumber;
	private String			title;
	private String			identifier;
	private String			banner;
	private Double			price;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	@NotNull
	@Valid
	public GPSCoordinates getCoordinates() {
		return this.coordinates;
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
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^([A-Za-z]){1,4}$")
	public String getIdentifier() {
		return this.identifier;
	}

	@URL
	public String getBanner() {
		return this.banner;
	}

	@NotNull
	@Digits(integer = 4, fraction = 2)
	@DecimalMin("1.0")
	public Double getPrice() {
		return this.price;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setCoordinates(final GPSCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

}
