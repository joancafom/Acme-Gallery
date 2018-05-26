
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Guide extends Actor {

	// Relationships ----------------------------------------------------------------------------------

	private Collection<Incident>	incidents;
	private Collection<Exhibition>	exhibitions;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "guide")
	public Collection<Incident> getIncidents() {
		return this.incidents;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<Exhibition> getExhibitions() {
		return this.exhibitions;
	}

	public void setIncidents(final Collection<Incident> incidents) {
		this.incidents = incidents;
	}

	public void setExhibitions(final Collection<Exhibition> exhibitions) {
		this.exhibitions = exhibitions;
	}

}
