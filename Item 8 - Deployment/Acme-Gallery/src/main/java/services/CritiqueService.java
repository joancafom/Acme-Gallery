
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CritiqueRepository;
import security.LoginService;
import domain.Critic;
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

	@Autowired
	private Validator			validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	//v1.0 - JA
	public Critique create(final Exhibition exhibition) {

		Assert.notNull(exhibition);

		final Critic currentCritic = this.criticService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentCritic);

		final Date now = new Date();
		Assert.isTrue(now.before(exhibition.getStartingDate()));

		final Critique res = new Critique();

		res.setExhibition(exhibition);
		res.setCritic(currentCritic);
		res.setCreationDate(new Date(System.currentTimeMillis() - 1000L));

		return res;
	}

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

		Assert.notNull(critique);

		return this.critiqueRepository.save(critique);
	}

	// v1.0 - Alicia
	public void flush() {
		this.critiqueRepository.flush();
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

	// v1.0 - JA
	public Critique findByCriticAndExhibition(final Critic critic, final Exhibition exhibition) {

		Assert.notNull(critic);
		Assert.notNull(exhibition);

		return this.critiqueRepository.findByCriticIdAndExhibitionId(critic.getId(), exhibition.getId());
	}

	//v1.0 - Implemented by JA
	public Critique reconstructCreate(final Critique prunedCritique, final BindingResult binding) {

		Assert.notNull(prunedCritique);
		Assert.notNull(prunedCritique.getExhibition());

		final Critique res = this.create(prunedCritique.getExhibition());

		res.setTitle(prunedCritique.getTitle());
		res.setDescription(prunedCritique.getDescription());
		res.setScore(prunedCritique.getScore());

		final Critic currentCritic = this.criticService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentCritic);

		//Check that it does not have a critique already
		Assert.isNull(this.findByCriticAndExhibition(currentCritic, prunedCritique.getExhibition()));

		this.validator.validate(res, binding);

		return res;
	}

	// v1.0 - JA
	public Critique saveCreate(final Critique critique) {

		Assert.notNull(critique);
		Assert.isTrue(critique.getId() == 0);
		Assert.notNull(critique.getExhibition());

		final Critic currentCritic = this.criticService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentCritic);

		Assert.isTrue(currentCritic.equals(critique.getCritic()));

		//Assert the exhibition has not passed
		final Date now = new Date();
		Assert.notNull(critique.getExhibition());
		Assert.isTrue(now.before(critique.getExhibition().getStartingDate()));

		//Assert that she or he does not already have a critique
		Assert.isNull(this.findByCriticAndExhibition(currentCritic, critique.getExhibition()));

		return this.critiqueRepository.save(critique);
	}
}
