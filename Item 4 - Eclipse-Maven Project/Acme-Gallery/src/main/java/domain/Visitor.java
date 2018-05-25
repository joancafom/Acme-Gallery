
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Visitor extends Actor {

	// Relationships ----------------------------------------------------------------------------------

	private Collection<Group>		createdGroups;
	private Collection<Group>		joinedGroups;
	private Collection<Comment>		comments;
	private Collection<DayPass>		dayPasses;
	private Collection<Review>		reviews;
	private Collection<Invitation>	receivedInvitations;
	private Collection<Invitation>	sentInvitations;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "creator")
	public Collection<Group> getCreatedGroups() {
		return this.createdGroups;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<Group> getJoinedGroups() {
		return this.joinedGroups;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "visitor")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "visitor")
	public Collection<DayPass> getDayPasses() {
		return this.dayPasses;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "visitor")
	public Collection<Review> getReviews() {
		return this.reviews;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "guest")
	public Collection<Invitation> getReceivedInvitations() {
		return this.receivedInvitations;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "host")
	public Collection<Invitation> getSentInvitations() {
		return this.sentInvitations;
	}

	public void setCreatedGroups(final Collection<Group> createdGroups) {
		this.createdGroups = createdGroups;
	}

	public void setJoinedGroups(final Collection<Group> joinedGroups) {
		this.joinedGroups = joinedGroups;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	public void setDayPasses(final Collection<DayPass> dayPasses) {
		this.dayPasses = dayPasses;
	}

	public void setReviews(final Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public void setReceivedInvitations(final Collection<Invitation> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
	}

	public void setSentInvitations(final Collection<Invitation> sentInvitations) {
		this.sentInvitations = sentInvitations;
	}

}
