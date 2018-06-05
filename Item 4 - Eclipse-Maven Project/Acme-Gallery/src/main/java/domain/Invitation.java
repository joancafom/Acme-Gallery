
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "sentMoment, isAccepted")
})
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


	// Relationships ----------------------------------------------------------------------------------

	private Group	group;
	private Visitor	host;
	private Visitor	guest;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Group getGroup() {
		return this.group;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getHost() {
		return this.host;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getGuest() {
		return this.guest;
	}

	public void setGroup(final Group group) {
		this.group = group;
	}

	public void setHost(final Visitor host) {
		this.host = host;
	}

	public void setGuest(final Visitor guest) {
		this.guest = guest;
	}

}
