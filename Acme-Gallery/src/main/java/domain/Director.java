
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
public class Director extends Actor {

	// Relationships ----------------------------------------------------------------------------------

	private Collection<Museum>	museums;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "director")
	public Collection<Museum> getMuseums() {
		return this.museums;
	}

	public void setMuseums(final Collection<Museum> museums) {
		this.museums = museums;
	}

}
