
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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


	// Relationships ----------------------------------------------------------------------------------

	private Museum				museum;
	private Collection<Product>	products;


	@NotNull
	@Valid
	@OneToOne(optional = false, mappedBy = "store")
	public Museum getMuseum() {
		return this.museum;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
	public Collection<Product> getProducts() {
		return this.products;
	}

	public void setMuseum(final Museum museum) {
		this.museum = museum;
	}

	public void setProducts(final Collection<Product> products) {
		this.products = products;
	}

}
