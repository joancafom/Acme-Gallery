
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "AGGroup", indexes = {
	@Index(columnList = "isClosed, containsTaboo")
})
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
	@NotNull
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
	public boolean getIsClosed() {
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

	public void setIsClosed(final boolean isClosed) {
		this.isClosed = isClosed;
	}

	public void setContainsTaboo(final boolean containsTaboo) {
		this.containsTaboo = containsTaboo;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Collection<Announcement>	announcements;
	private Collection<Comment>			comments;
	private Visitor						creator;
	private Collection<Visitor>			participants;
	private Collection<Invitation>		invitations;
	private Museum						museum;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "group")
	public Collection<Announcement> getAnnouncements() {
		return this.announcements;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "group")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getCreator() {
		return this.creator;
	}

	@NotNull
	@Valid
	@ManyToMany(mappedBy = "joinedGroups")
	public Collection<Visitor> getParticipants() {
		return this.participants;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "group")
	public Collection<Invitation> getInvitations() {
		return this.invitations;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Museum getMuseum() {
		return this.museum;
	}

	public void setAnnouncements(final Collection<Announcement> announcements) {
		this.announcements = announcements;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	public void setCreator(final Visitor creator) {
		this.creator = creator;
	}

	public void setParticipants(final Collection<Visitor> participants) {
		this.participants = participants;
	}

	public void setInvitations(final Collection<Invitation> invitations) {
		this.invitations = invitations;
	}

	public void setMuseum(final Museum museum) {
		this.museum = museum;
	}

}
