
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Invitation extends DomainEntity {

	private String	message;
	private Date	sentMoment;
	private Boolean	isAccepted;


	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getMessage() {
		return this.message;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSentMoment() {
		return this.sentMoment;
	}

	public Boolean getIsAccepted() {
		return this.isAccepted;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setSentMoment(final Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	public void setIsAccepted(final Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

}
