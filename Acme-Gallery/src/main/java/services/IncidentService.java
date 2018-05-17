
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.IncidentRepository;
import domain.Director;
import domain.Incident;

@Service
@Transactional
public class IncidentService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private IncidentRepository	incidentRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

	public Collection<Incident> getByDirector(final Director director) {
		Assert.notNull(director);

		final Collection<Incident> res = this.incidentRepository.findByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

}
