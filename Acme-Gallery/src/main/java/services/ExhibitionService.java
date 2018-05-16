
package services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ExhibitionRepository;
import security.LoginService;
import domain.Category;
import domain.Critique;
import domain.DayPass;
import domain.Director;
import domain.Exhibition;
import domain.Guide;
import domain.Highlight;
import domain.Museum;
import domain.Sponsorship;
import forms.ExhibitionForm;

@Service
@Transactional
public class ExhibitionService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ExhibitionRepository	exhibitionRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService			directorService;

	@Autowired
	private GuideService			guideService;

	@Autowired
	private RoomService				roomService;

	// Validator --------------------------------------------------------------------------------------

	@Autowired
	private Validator				validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Exhibition create() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Exhibition exhibition = new Exhibition();

		exhibition.setDayPasses(new HashSet<DayPass>());
		exhibition.setSponsorships(new HashSet<Sponsorship>());
		exhibition.setCritiques(new HashSet<Critique>());

		exhibition.setHighlights(new HashSet<Highlight>());
		exhibition.setGuides(new HashSet<Guide>());

		return exhibition;
	}

	// v1.0 - JA
	public Exhibition findOne(final int exhibitionId) {

		return this.exhibitionRepository.findOne(exhibitionId);
	}

	// v1.0 - JA
	public Exhibition save(final Exhibition exhibition) {

		Assert.notNull(exhibition);

		//Beware to modify this method! CategoryService.delete uses it

		return this.exhibitionRepository.save(exhibition);
	}

	// v1.0 - Alicia
	public Exhibition saveCreateAndEdit(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().before(exhibition.getEndingDate()));
		Assert.isTrue((exhibition.getIsPrivate() && exhibition.getPrice() > 0.0) || (!exhibition.getIsPrivate() && exhibition.getPrice() == 0.0));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));

		if (exhibition.getId() == 0) {
			exhibition.getRoom().setIsAvailable(false);
			this.roomService.save(exhibition.getRoom());

			exhibition.setIdentifier(director.getUserAccount().getUsername() + "-" + exhibition.getIdentifier());
		}

		Assert.notNull(exhibition.getWebsites());
		if (!exhibition.getWebsites().isEmpty())
			for (final String s : exhibition.getWebsites())
				try {
					@SuppressWarnings("unused")
					final URL url = new java.net.URL(s);
				} catch (final MalformedURLException e) {
					throw new IllegalArgumentException();
				}

		return this.exhibitionRepository.save(exhibition);
	}

	// v1.0 - JA
	public void flush() {

		this.exhibitionRepository.flush();
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - Alicia
	public Collection<Exhibition> getByKeyword(final String keyword) {
		Assert.notNull(keyword);

		final Collection<Exhibition> res = this.exhibitionRepository.findByKeyword(keyword);
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getByKeyword(final String keyword, final int page, final int size) {
		Assert.notNull(keyword);

		final Page<Exhibition> res = this.exhibitionRepository.findByKeyword(keyword, new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getByCategory(final Category category) {
		Assert.notNull(category);

		final Collection<Exhibition> res = this.exhibitionRepository.findByCategoryId(category.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getByCategory(final Category category, final int page, final int size) {
		Assert.notNull(category);

		final Page<Exhibition> res = this.exhibitionRepository.findByCategoryId(category.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Collection<Exhibition> getCurrentAndFutureByMuseum(final Museum museum) {
		Assert.notNull(museum);

		final Collection<Exhibition> res = this.exhibitionRepository.getCurrentAndFutureByMuseumId(museum.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Exhibition> getCurrentAndFutureByMuseum(final Museum museum, final int page, final int size) {
		Assert.notNull(museum);

		final Page<Exhibition> res = this.exhibitionRepository.getCurrentAndFutureByMuseumId(museum.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Collection<Exhibition> getAllByMuseum(final Museum museum) {
		Assert.notNull(museum);

		final Collection<Exhibition> res = this.exhibitionRepository.getAllByMuseumId(museum.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Exhibition> getAllByMuseum(final Museum museum, final int page, final int size) {
		Assert.notNull(museum);

		final Page<Exhibition> res = this.exhibitionRepository.getAllByMuseumId(museum.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getByDirector(final Director director) {
		Assert.notNull(director);

		final Collection<Exhibition> res = this.exhibitionRepository.findByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getByDirector(final Director director, final int page, final int size) {
		Assert.notNull(director);

		final Page<Exhibition> res = this.exhibitionRepository.findByDirectorId(director.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Exhibition reconstructSave(final Exhibition prunedExhibition, final BindingResult binding) {
		Assert.notNull(prunedExhibition);

		final Exhibition res = this.create();
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());

		if (prunedExhibition.getId() == 0) {

			res.setIdentifier(director.getUserAccount().getUsername() + "-" + prunedExhibition.getIdentifier());
			res.setTitle(prunedExhibition.getTitle());
			res.setDescription(prunedExhibition.getDescription());
			res.setStartingDate(prunedExhibition.getStartingDate());
			res.setEndingDate(prunedExhibition.getEndingDate());
			res.setWebsites(prunedExhibition.getWebsites());
			res.setIsPrivate(prunedExhibition.getIsPrivate());
			res.setPrice(prunedExhibition.getPrice());

			res.setCategory(prunedExhibition.getCategory());
			res.setRoom(prunedExhibition.getRoom());

		} else {

			final Exhibition oldExhibition = this.findOne(prunedExhibition.getId());

			res.setId(oldExhibition.getId());

			res.setIdentifier(oldExhibition.getIdentifier());

			res.setTitle(prunedExhibition.getTitle());
			res.setDescription(prunedExhibition.getDescription());

			res.setStartingDate(oldExhibition.getStartingDate());
			res.setEndingDate(oldExhibition.getEndingDate());

			res.setWebsites(prunedExhibition.getWebsites());

			res.setIsPrivate(oldExhibition.getIsPrivate());
			res.setPrice(oldExhibition.getPrice());

			res.setDayPasses(oldExhibition.getDayPasses());
			res.setSponsorships(oldExhibition.getSponsorships());
			res.setCritiques(oldExhibition.getCritiques());
			res.setCategory(oldExhibition.getCategory());
			res.setHighlights(oldExhibition.getHighlights());
			res.setGuides(oldExhibition.getGuides());
			res.setRoom(oldExhibition.getRoom());

		}

		this.validator.validate(res, binding);

		if (prunedExhibition.getId() == 0)
			res.setIdentifier(prunedExhibition.getIdentifier());

		return res;
	}

	// v1.0 - Alicia
	public Exhibition reconstructSave(final ExhibitionForm exhibitionForm, final BindingResult binding) {
		Assert.notNull(exhibitionForm);

		final Exhibition res = this.create();

		final Exhibition oldExhibition = this.findOne(exhibitionForm.getExhibition().getId());

		res.setId(oldExhibition.getId());

		res.setIdentifier(oldExhibition.getIdentifier());
		res.setTitle(oldExhibition.getTitle());
		res.setDescription(oldExhibition.getDescription());
		res.setStartingDate(oldExhibition.getStartingDate());
		res.setEndingDate(oldExhibition.getEndingDate());
		res.setWebsites(oldExhibition.getWebsites());
		res.setIsPrivate(oldExhibition.getIsPrivate());
		res.setPrice(oldExhibition.getPrice());

		res.setDayPasses(oldExhibition.getDayPasses());
		res.setSponsorships(oldExhibition.getSponsorships());
		res.setCritiques(oldExhibition.getCritiques());
		res.setCategory(oldExhibition.getCategory());
		res.setHighlights(oldExhibition.getHighlights());

		final Collection<Guide> newGuides = new HashSet<Guide>();
		newGuides.addAll(oldExhibition.getGuides());

		for (final String s : exhibitionForm.getGuides())
			newGuides.add(this.guideService.findOne(new Integer(s)));

		res.setGuides(newGuides);

		res.setRoom(oldExhibition.getRoom());

		this.validator.validate(res, binding);

		return res;
	}
}
