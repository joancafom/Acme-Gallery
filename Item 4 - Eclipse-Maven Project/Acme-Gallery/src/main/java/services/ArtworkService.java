
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArtworkRepository;
import security.LoginService;
import domain.Artwork;
import domain.Exhibition;
import domain.Guide;

@Service
@Transactional
public class ArtworkService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ArtworkRepository	artworkRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private Validator			validator;

	@Autowired
	private GuideService		guideService;

	@Autowired
	private ExhibitionService	exhibitionService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Artwork findOne(final int artworkId) {
		return this.artworkRepository.findOne(artworkId);
	}

	/* v1.0 - josembell */
	public Artwork create(final Exhibition exhibition) {
		Assert.notNull(exhibition);
		final Artwork artwork = new Artwork();

		artwork.setExhibition(exhibition);

		return artwork;
	}

	/* v1.0 - josembell */
	public Artwork save(final Artwork artwork) {
		Assert.notNull(artwork);
		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(guide);
		Assert.isTrue(artwork.getExhibition().getRoom().getMuseum().getGuides().contains(guide));

		if (artwork.getId() != 0)
			Assert.isTrue(artwork.getIsFinal() == false);

		final Integer yearOfNow = LocalDate.now().getYear();
		if (artwork.getYear() != null) {
			Assert.isTrue(artwork.getYear() % 1 == 0);
			Assert.isTrue(artwork.getYear() <= yearOfNow);
		}

		final Artwork saved = this.artworkRepository.save(artwork);
		artwork.getExhibition().getArtworks().add(saved);
		this.exhibitionService.save(saved.getExhibition());

		return saved;
	}

	/* v1.0 - josembell */
	public void delete(final Artwork artwork) {
		Assert.notNull(artwork);
		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(guide);
		Assert.isTrue(artwork.getExhibition().getRoom().getMuseum().getGuides().contains(guide));
		Assert.isTrue(artwork.getIsFinal() == false);

		this.artworkRepository.delete(artwork);

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

	// v1.0 - Alicia
	public Collection<Artwork> findFinalByExhibition(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Collection<Artwork> res = this.artworkRepository.findFinalByExhibitionId(exhibition.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Artwork> findFinalByExhibition(final Exhibition exhibition, final int page, final int size) {
		Assert.notNull(exhibition);

		final Page<Artwork> res = this.artworkRepository.findFinalByExhibitionId(exhibition.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Artwork reconstruct(final Artwork prunedArtwork, final BindingResult binding) {
		Assert.notNull(prunedArtwork);

		final Artwork artwork;

		artwork = prunedArtwork;

		this.validator.validate(artwork, binding);

		return artwork;
	}

}
