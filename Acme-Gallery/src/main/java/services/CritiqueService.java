
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CritiqueRepository;
import domain.Critique;
import domain.Exhibition;

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

	// v1.0 - JA
	public Collection<Critique> findAllByExhibition(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Collection<Critique> res = this.critiqueRepository.findAllByExhibitionId(exhibition.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Critique> findAllByExhibition(final Exhibition exhibition, final int page, final int size) {
		Assert.notNull(exhibition);

		final Page<Critique> res = this.critiqueRepository.findAllByExhibitionId(exhibition.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

}