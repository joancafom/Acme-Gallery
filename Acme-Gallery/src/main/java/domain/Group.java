
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Group extends DomainEntity {

	private String	name;
	private String	description;
	private Date	creationMoment;
	private Integer	maxParticipants;
	private Date	meetingDate;
	private boolean	isClosed;
	private boolean	containsTaboo;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@Length(max = 250)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getCreationMoment() {
		return this.creationMoment;
	}

	@Min(1)
	public Integer getMaxParticipants() {
		return this.maxParticipants;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMeetingDate() {
		return this.meetingDate;
	}

	@NotNull
	public boolean isClosed() {
		return this.isClosed;
	}

	@NotNull
	public boolean isContainsTaboo() {
		return this.containsTaboo;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setCreationMoment(final Date creationMoment) {
		this.creationMoment = creationMoment;
	}

	public void setMaxParticipants(final Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public void setMeetingDate(final Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public void setClosed(final boolean isClosed) {
		this.isClosed = isClosed;
	}

	public void setContainsTaboo(final boolean containsTaboo) {
		this.containsTaboo = containsTaboo;
	}

}
