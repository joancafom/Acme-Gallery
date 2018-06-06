
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isHighlight, isFinal")
})
public class Artwork extends DomainEntity {

	private String	title;
	private String	photograph;
	private String	creatorName;
	private String	remark;
	private Integer	year;
	private boolean	isFinal;
	private boolean	isHighlight;


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
	@Column(columnDefinition = "TEXT")
	public String getRemark() {
		return this.remark;
	}

	public Integer getYear() {
		return this.year;
	}

	@NotNull
	public boolean getIsFinal() {
		return this.isFinal;
	}

	@NotNull
	public boolean getIsHighlight() {
		return this.isHighlight;
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

	public void setIsFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}

	public void setIsHighlight(final boolean isHighlight) {
		this.isHighlight = isHighlight;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Exhibition	exhibition;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Exhibition getExhibition() {
		return this.exhibition;
	}

	public void setExhibition(final Exhibition exhibition) {
		this.exhibition = exhibition;
	}

}
