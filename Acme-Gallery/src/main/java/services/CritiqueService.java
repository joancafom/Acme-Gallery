
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CritiqueRepository;
import security.LoginService;
import domain.Critique;
import domain.Director;
import domain.Exhibition;

@Service
@Transactional
public class CritiqueService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private CritiqueRepository	critiqueRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private CriticService		criticService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public void delete(final Critique critique) {
		Assert.notNull(critique);
		Assert.isTrue(this.critiqueRepository.exists(critique.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(critique.getExhibition().getRoom().getMuseum()));
		Assert.isTrue(critique.getExhibition().getStartingDate().after(new Date()));
		Assert.isTrue(critique.getExhibition().getDayPasses().isEmpty());
		Assert.isTrue(critique.getExhibition().getSponsorships().isEmpty());

		critique.getCritic().getCritiques().remove(critique);
		this.criticService.save(critique.getCritic());

		critique.getExhibition().getCritiques().remove(critique);

		this.critiqueRepository.delete(critique);
	}

	// v1.0 - Alicia
	public void deleteRoom(final Critique critique) {
		Assert.notNull(critique);
		Assert.isTrue(this.critiqueRepository.exists(critique.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(critique.getExhibition().getRoom().getMuseum()));
		Assert.isTrue(critique.getExhibition().getDayPasses().isEmpty() || critique.getExhibition().getEndingDate().before(new Date()));
		Assert.isTrue(critique.getExhibition().getSponsorships().isEmpty() || critique.getExhibition().getEndingDate().before(new Date()));

		critique.getCritic().getCritiques().remove(critique);
		this.criticService.save(critique.getCritic());

		this.critiqueRepository.delete(critique);
	}

	// v1.0 - Alicia
	public Critique findOne(final int critiqueId) {
		return this.critiqueRepository.findOne(critiqueId);
	}

	// v1.0 - Alicia
	public Critique save(final Critique critique) {
		return this.critiqueRepository.save(critique);
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
