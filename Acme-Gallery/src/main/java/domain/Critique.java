
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Critique extends DomainEntity {

	private String	title;
	private String	description;
	private Integer	score;
	private Date	creationDate;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return this.description;
	}

	@NotNull
	@Min(1)
	@Max(5)
	public Integer getScore() {
		return this.score;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setScore(final Integer score) {
		this.score = score;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Critic		critic;
	private Exhibition	exhibition;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Critic getCritic() {
		return this.critic;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Exhibition getExhibition() {
		return this.exhibition;
	}

	public void setCritic(final Critic critic) {
		this.critic = critic;
	}

	public void setExhibition(final Exhibition exhibition) {
		this.exhibition = exhibition;
	}

}
