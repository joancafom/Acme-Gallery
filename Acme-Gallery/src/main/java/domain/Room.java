
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
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
	private boolean	inRepair;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotNull
	@DecimalMin("1.0")
	public Double getArea() {
		return this.area;
	}

	@NotNull
	public boolean getInRepair() {
		return this.inRepair;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setArea(final Double area) {
		this.area = area;
	}

	public void setInRepair(final boolean inRepair) {
		this.inRepair = inRepair;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Museum					museum;
	private Collection<Exhibition>	exhibitions;
	private Collection<Incident>	incidents;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Museum getMuseum() {
		return this.museum;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "room")
	public Collection<Exhibition> getExhibitions() {
		return this.exhibitions;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "room")
	public Collection<Incident> getIncidents() {
		return this.incidents;
	}

	public void setMuseum(final Museum museum) {
		this.museum = museum;
	}

	public void setExhibitions(final Collection<Exhibition> exhibitions) {
		this.exhibitions = exhibitions;
	}

	public void setIncidents(final Collection<Incident> incidents) {
		this.incidents = incidents;
	}

}
