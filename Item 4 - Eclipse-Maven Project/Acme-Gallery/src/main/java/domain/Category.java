
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "name")
})
public class Category extends DomainEntity {

	private String	name;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Collection<Category>	childrenCategories;
	private Category				parentCategory;
	private Collection<Exhibition>	exhibitions;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "parentCategory")
	public Collection<Category> getChildrenCategories() {
		return this.childrenCategories;
	}

	@Valid
	@ManyToOne(optional = true)
	public Category getParentCategory() {
		return this.parentCategory;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "category")
	public Collection<Exhibition> getExhibitions() {
		return this.exhibitions;
	}

	public void setChildrenCategories(final Collection<Category> childrenCategories) {
		this.childrenCategories = childrenCategories;
	}

	public void setParentCategory(final Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public void setExhibitions(final Collection<Exhibition> exhibitions) {
		this.exhibitions = exhibitions;
	}

}
