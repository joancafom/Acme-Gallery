
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Embeddable
@Access(AccessType.PROPERTY)
public class CreditCard {

	private String	holderName;
	private String	brandName;
	private String	number;
	private Integer	CVV;
	private Integer	month;
	private Integer	year;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getHolderName() {
		return this.holderName;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBrandName() {
		return this.brandName;
	}

	@NotNull
	@Pattern(regexp = "(^[0-9]+$)")
	@CreditCardNumber
	public String getNumber() {
		return this.number;
	}

	@Min(100)
	@Max(999)
	@NotNull
	public Integer getCVV() {
		return this.CVV;
	}

	@Min(1)
	@Max(12)
	@NotNull
	public Integer getMonth() {
		return this.month;
	}

	@NotNull
	public Integer getYear() {
		return this.year;
	}

	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}

	public void setBrandName(final String brandName) {
		this.brandName = brandName;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setCVV(final Integer CVV) {
		this.CVV = CVV;
	}

	public void setMonth(final Integer month) {
		this.month = month;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}

}
