
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.CritiqueRepository;
import domain.Critique;

@Service
@Transactional
public class CritiqueService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private CritiqueRepository	critiqueRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Critique findOne(final int critiqueId) {
		return this.critiqueRepository.findOne(critiqueId);
	}

	//Other Business Methods --------------------------------------------------------------------------

}
