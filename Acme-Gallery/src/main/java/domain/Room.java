
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Room extends DomainEntity {

	private String	name;
	private Double	area;
	private boolean	isAvailable;
	private boolean	inRepair;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotNull
	@DecimalMin("0.0")
	public Double getArea() {
		return this.area;
	}

	@NotNull
	public boolean isAvailable() {
		return this.isAvailable;
	}

	@NotNull
	public boolean isInRepair() {
		return this.inRepair;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setArea(final Double area) {
		this.area = area;
	}

	public void setAvailable(final boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public void setInRepair(final boolean inRepair) {
		this.inRepair = inRepair;
	}

}
