
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
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
	public boolean isChecked() {
		return this.isChecked;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

	public void setChecked(final boolean isChecked) {
		this.isChecked = isChecked;
	}

}
