
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Critic extends Actor {

	// Relationships ----------------------------------------------------------------------------------

	private Collection<Critique>	critiques;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "critic")
	public Collection<Critique> getCritiques() {
		return this.critiques;
	}

	public void setCritiques(final Collection<Critique> critiques) {
		this.critiques = critiques;
	}

}
