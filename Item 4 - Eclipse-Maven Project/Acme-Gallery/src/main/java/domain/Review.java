
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
public class Review extends DomainEntity {

	private String	body;
	private Integer	score;
	private boolean	containsTaboo;
	private Date	creationDate;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBody() {
		return this.body;
	}

	@NotNull
	@Min(1)
	@Max(5)
	public Integer getScore() {
		return this.score;
	}

	@NotNull
	public boolean isContainsTaboo() {
		return this.containsTaboo;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public void setScore(final Integer score) {
		this.score = score;
	}

	public void setContainsTaboo(final boolean containsTaboo) {
		this.containsTaboo = containsTaboo;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Visitor	visitor;
	private Museum	museum;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getVisitor() {
		return this.visitor;
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

	public void setMuseum(final Museum museum) {
		this.museum = museum;
	}

}
