
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends DomainEntity {

	private String				name;
	private String				description;
	private Collection<String>	pictures;
	private Double				price;
	private String				barcode;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getPictures() {
		return this.pictures;
	}

	@NotNull
	@Digits(integer = 4, fraction = 2)
	@DecimalMin("0.0")
	public Double getPrice() {
		return this.price;
	}

	@NotBlank
	@Pattern(regexp = "^[0-9]{13}$")
	public String getBarcode() {
		return this.barcode;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	public void setBarcode(final String barcode) {
		this.barcode = barcode;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Store	store;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Store getStore() {
		return this.store;
	}

	public void setStore(final Store store) {
		this.store = store;
	}

}
