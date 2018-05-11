
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Highlight extends DomainEntity {

	private String	title;
	private String	photograph;
	private String	creatorName;
	private String	remark;
	private Integer	year;
	private boolean	isFinal;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@URL
	public String getPhotograph() {
		return this.photograph;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCreatorName() {
		return this.creatorName;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getRemark() {
		return this.remark;
	}

	public Integer getYear() {
		return this.year;
	}

	@NotNull
	public boolean isFinal() {
		return this.isFinal;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setPhotograph(final String photograph) {
		this.photograph = photograph;
	}

	public void setCreatorName(final String creatorName) {
		this.creatorName = creatorName;
	}

	public void setRemark(final String remark) {
		this.remark = remark;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}

	public void setFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}

}
