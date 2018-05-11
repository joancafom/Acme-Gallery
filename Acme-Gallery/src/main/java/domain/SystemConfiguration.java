
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	private String	tabooWords;
	private Double	VAT;


	@NotNull
	@Pattern(regexp = "^[\\w\\|]*$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTabooWords() {
		return this.tabooWords;
	}

	@NotNull
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	public Double getVAT() {
		return this.VAT;
	}

	public void setTabooWords(final String tabooWords) {
		this.tabooWords = tabooWords;
	}

	public void setVAT(final Double vAT) {
		this.VAT = vAT;
	}

}
