
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class DayPass extends DomainEntity {

	private String		ticker;
	private Date		purchaseMoment;
	private Date		visitDate;
	private CreditCard	creditCard;
	private Double		price;
	private Double		VAT;


	@NotNull
	@Pattern(regexp = "^([A-Za-z]{1,4}-[A-Za-z0-9]{5,}-[0-9]{4})$")
	public String getTicker() {
		return this.ticker;
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

	@NotNull
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	public Double getVAT() {
		return this.VAT;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
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

	public void setVAT(final Double vAT) {
		this.VAT = vAT;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Visitor		visitor;
	private Exhibition	exhibition;
	private Museum		museum;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getVisitor() {
		return this.visitor;
	}

	@Valid
	@ManyToOne(optional = true)
	public Exhibition getExhibition() {
		return this.exhibition;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Museum getMuseum() {
		return this.museum;
	}

	public void setVisitor(final Visitor visitor) {
		this.visitor = visitor;
	}

	public void setExhibition(final Exhibition exhibition) {
		this.exhibition = exhibition;
	}

	public void setMuseum(final Museum museum) {
		this.museum = museum;
	}

}
