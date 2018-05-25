
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	private String	title;
	private String	description;
	private String	picture;
	private boolean	containsTaboo;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	@NotNull
	public boolean isContainsTaboo() {
		return this.containsTaboo;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setContainsTaboo(final boolean containsTaboo) {
		this.containsTaboo = containsTaboo;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Collection<Comment>	childrenComments;
	private Comment				parentComment;
	private Visitor				visitor;
	private Group				group;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "parentComment")
	public Collection<Comment> getChildrenComments() {
		return this.childrenComments;
	}

	@Valid
	@ManyToOne(optional = true)
	public Comment getParentComment() {
		return this.parentComment;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getVisitor() {
		return this.visitor;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Group getGroup() {
		return this.group;
	}

	public void setChildrenComments(final Collection<Comment> childrenComments) {
		this.childrenComments = childrenComments;
	}

	public void setParentComment(final Comment parentComment) {
		this.parentComment = parentComment;
	}

	public void setVisitor(final Visitor visitor) {
		this.visitor = visitor;
	}

	public void setGroup(final Group group) {
		this.group = group;
	}

}
