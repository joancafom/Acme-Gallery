
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.URL.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Exhibition extends DomainEntity {

	private String				identifier;
	private String				title;
	private String				description;
	private Date				startingDate;
	private Date				endingDate;
	private Collection<String>	websites;
	private boolean				isPrivate;
	private Double				price;


	@NotBlank
	@Pattern(regexp = "^([A-Za-z]{5,}-[A-Za-z0-9]{4,})$")
	@Column(unique = true)
	public String getIdentifier() {
		return this.identifier;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getStartingDate() {
		return this.startingDate;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEndingDate() {
		return this.endingDate;
	}

	@NotEmpty
	@ElementCollection
	@List(value = {
		@URL
	})
	public Collection<String> getWebsites() {
		return this.websites;
	}

	@NotNull
	public boolean isPrivate() {
		return this.isPrivate;
	}

	@NotNull
	@Digits(integer = 4, fraction = 2)
	@DecimalMin("1.0")
	public Double getPrice() {
		return this.price;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setStartingDate(final Date startingDate) {
		this.startingDate = startingDate;
	}

	public void setEndingDate(final Date endingDate) {
		this.endingDate = endingDate;
	}

	public void setWebsites(final Collection<String> websites) {
		this.websites = websites;
	}

	public void setPrivate(final boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

}
