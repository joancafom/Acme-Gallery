
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GuideRepository;
import domain.Exhibition;
import domain.Guide;

@Service
@Transactional
public class GuideService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private GuideRepository	guideRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - JA
	public Collection<Guide> findAllByExhibition(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Collection<Guide> res = this.guideRepository.findAllByExhibition(exhibition);
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Guide> findAllByExhibition(final Exhibition exhibition, final int page, final int size) {
		Assert.notNull(exhibition);

		final Page<Guide> res = this.guideRepository.findAllByExhibition(exhibition, new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

}
