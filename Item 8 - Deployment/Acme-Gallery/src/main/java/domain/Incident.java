
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isChecked, level")
})
public class Incident extends DomainEntity {

	private String	text;
	private String	level;
	private boolean	isChecked;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	@NotNull
	@Pattern(regexp = "^(LOW|MEDIUM|HIGH)$")
	public String getLevel() {
		return this.level;
	}

	@NotNull
	public boolean getIsChecked() {
		return this.isChecked;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

	public void setIsChecked(final boolean isChecked) {
		this.isChecked = isChecked;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Guide	guide;
	private Room	room;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Guide getGuide() {
		return this.guide;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Room getRoom() {
		return this.room;
	}

	public void setGuide(final Guide guide) {
		this.guide = guide;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

}
