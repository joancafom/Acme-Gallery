
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class DayPass extends DomainEntity {

	private String		identifier;
	private Date		purchaseMoment;
	private Date		visitDate;
	private CreditCard	creditCard;
	private Double		price;


	@NotNull
	@Pattern(regexp = "^([A-Za-z]{1,4}-[A-Za-z]{5,}-[0-9]{4})$")
	public String getIdentifier() {
		return this.identifier;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getPurchaseMoment() {
		return this.purchaseMoment;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getVisitDate() {
		return this.visitDate;
	}

	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
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

	public void setPurchaseMoment(final Date purchaseMoment) {
		this.purchaseMoment = purchaseMoment;
	}

	public void setVisitDate(final Date visitDate) {
		this.visitDate = visitDate;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

}
