
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	private String		status;
	private CreditCard	creditCard;
	private String		banner;
	private String		link;
	private String		startingDate;
	private String		endingDate;


	@NotNull
	@Pattern(regexp = "^(ACCEPTED|PENDING|TIME_NEGOCIATION|REJECTED)$")
	public String getStatus() {
		return this.status;
	}

	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	@NotBlank
	@URL
	public String getLink() {
		return this.link;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public String getStartingDate() {
		return this.startingDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public String getEndingDate() {
		return this.endingDate;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setStartingDate(final String startingDate) {
		this.startingDate = startingDate;
	}

	public void setEndingDate(final String endingDate) {
		this.endingDate = endingDate;
	}

}
