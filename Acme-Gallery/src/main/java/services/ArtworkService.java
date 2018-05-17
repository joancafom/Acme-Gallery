
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ArtworkRepository;
import domain.Artwork;
import domain.Exhibition;

@Service
@Transactional
public class ArtworkService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ArtworkRepository	artworkRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Artwork findOne(final int artworkId) {
		return this.artworkRepository.findOne(artworkId);
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - JA
	public Collection<Artwork> findAllByExhibition(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Collection<Artwork> res = this.artworkRepository.findAllByExhibitionId(exhibition.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Artwork> findAllByExhibition(final Exhibition exhibition, final int page, final int size) {
		Assert.notNull(exhibition);

		final Page<Artwork> res = this.artworkRepository.findAllByExhibitionId(exhibition.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

}
