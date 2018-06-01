
package services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import repositories.ExhibitionRepository;
import security.LoginService;
import domain.Artwork;
import domain.Category;
import domain.Critique;
import domain.DayPass;
import domain.Director;
import domain.Exhibition;
import domain.Guide;
import domain.Museum;
import domain.Room;
import domain.Sponsor;
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
	private CategoryService			categoryService;

	@Autowired
	private CritiqueService			critiqueService;

	@Autowired
	private DayPassService			dayPassService;

	@Autowired
	private DirectorService			directorService;

	@Autowired
	private GuideService			guideService;

	@Autowired
	private RoomService				roomService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private SponsorshipService		sponsorshipService;

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

		exhibition.setArtworks(new HashSet<Artwork>());
		exhibition.setGuides(new HashSet<Guide>());

		return exhibition;
	}

	// v1.0 - JA
	public Collection<Exhibition> findAll() {

		return this.exhibitionRepository.findAll();
	}

	// v1.0 - Alicia
	public void delete(final Exhibition exhibition) {
		Assert.notNull(exhibition);
		Assert.isTrue(this.exhibitionRepository.exists(exhibition.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));
		Assert.isTrue(exhibition.getDayPasses().isEmpty());
		Assert.isTrue(exhibition.getSponsorships().isEmpty());

		final Collection<Critique> critiques = new HashSet<Critique>(exhibition.getCritiques());
		for (final Critique c : critiques)
			this.critiqueService.delete(c);

		exhibition.getCategory().getExhibitions().remove(exhibition);
		this.categoryService.save(exhibition.getCategory());

		for (final Guide g : exhibition.getGuides()) {
			g.getExhibitions().remove(exhibition);
			this.guideService.save(g);
		}

		exhibition.getRoom().getExhibitions().remove(exhibition);
		this.roomService.save(exhibition.getRoom());

		this.exhibitionRepository.delete(exhibition);
	}

	// v1.0 - Alicia
	public void deleteRoom(final Exhibition exhibition) {
		Assert.notNull(exhibition);
		Assert.isTrue(this.exhibitionRepository.exists(exhibition.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getDayPasses().isEmpty() || exhibition.getEndingDate().before(new Date()));
		Assert.isTrue(exhibition.getSponsorships().isEmpty() || exhibition.getEndingDate().before(new Date()));

		for (final Critique c : exhibition.getCritiques())
			this.critiqueService.deleteRoom(c);

		exhibition.getCategory().getExhibitions().remove(exhibition);
		this.categoryService.save(exhibition.getCategory());

		for (final Guide g : exhibition.getGuides()) {
			g.getExhibitions().remove(exhibition);
			this.guideService.save(g);
		}

		for (final DayPass d : exhibition.getDayPasses())
			this.dayPassService.delete(d);

		for (final Sponsorship s : exhibition.getSponsorships())
			this.sponsorshipService.delete(s);

		this.exhibitionRepository.delete(exhibition);
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

	// v2.0 - Alicia
	public Exhibition saveCreateAndEdit(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		Assert.notNull(exhibition.getRoom());
		Assert.notNull(exhibition.getStartingDate());
		Assert.notNull(exhibition.getEndingDate());
		Assert.notNull(exhibition.getIsPrivate());
		Assert.notNull(exhibition.getPrice());
		Assert.notNull(exhibition.getTicker());

		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().before(exhibition.getEndingDate()));
		Assert.isTrue((exhibition.getIsPrivate() && exhibition.getPrice() > 0.0) || (!exhibition.getIsPrivate() && exhibition.getPrice() == 0.0));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));

		System.out.println(this.exhibitionRepository.findDateAndRoomConflicts(exhibition.getRoom().getId(), exhibition.getStartingDate(), exhibition.getEndingDate()));

		final Collection<Exhibition> concurrentExhibitions = this.exhibitionRepository.findDateAndRoomConflicts(exhibition.getRoom().getId(), exhibition.getStartingDate(), exhibition.getEndingDate());

		if (exhibition.getId() == 0) {
			Assert.isTrue(concurrentExhibitions.isEmpty());
			exhibition.setTicker(director.getUserAccount().getUsername() + "-" + exhibition.getTicker());
		} else {
			//There are two valid possibilities here

			//The dates we have selected do not conflict with any other exhibition
			//Or the do conflict with the exhibitions we are trying to modify (for example,
			//if we try to shorten the dates), but not with other exhibition different than
			//itself
			Assert.isTrue(concurrentExhibitions.size() <= 1);

			if (concurrentExhibitions.size() == 1)
				Assert.isTrue(concurrentExhibitions.contains(exhibition));
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
	// v2.0 - JA (Errors)
	// v3.0 - Alicia
	public Exhibition reconstructSave(final Exhibition prunedExhibition, final BindingResult binding) {
		Assert.notNull(prunedExhibition);

		final Exhibition res = this.create();
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());

		final String tickerPrefix = director.getUserAccount().getUsername() + "-";

		final Exhibition oldExhibition = this.findOne(prunedExhibition.getId());

		if (oldExhibition != null)
			Assert.isTrue(oldExhibition.getRoom().getMuseum().getDirector().equals(director));

		Assert.isTrue(prunedExhibition.getId() == 0 || oldExhibition != null);

		if (prunedExhibition.getId() == 0 || (oldExhibition != null && oldExhibition.getDayPasses().isEmpty())) {

			if (oldExhibition != null && oldExhibition.getDayPasses().isEmpty()) {
				res.setId(oldExhibition.getId());
				res.setVersion(oldExhibition.getVersion());
			}

			if (prunedExhibition.getId() == 0)
				res.setTicker(tickerPrefix + prunedExhibition.getTicker());
			else if (oldExhibition != null && oldExhibition.getDayPasses().isEmpty())
				res.setTicker(oldExhibition.getTicker());

			res.setTitle(prunedExhibition.getTitle());
			res.setDescription(prunedExhibition.getDescription());
			res.setStartingDate(prunedExhibition.getStartingDate());
			res.setEndingDate(prunedExhibition.getEndingDate());
			res.setWebsites(prunedExhibition.getWebsites());
			res.setIsPrivate(prunedExhibition.getIsPrivate());
			res.setPrice(prunedExhibition.getPrice());

			if (!prunedExhibition.getIsPrivate() && prunedExhibition.getPrice() == null)
				res.setPrice(0.0);

			res.setCategory(prunedExhibition.getCategory());
			res.setRoom(prunedExhibition.getRoom());

		} else if (oldExhibition != null && !oldExhibition.getDayPasses().isEmpty()) {

			res.setId(oldExhibition.getId());
			res.setVersion(oldExhibition.getVersion());

			res.setTicker(oldExhibition.getTicker());

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

			res.setCategory(prunedExhibition.getCategory());

			res.setArtworks(oldExhibition.getArtworks());
			res.setGuides(oldExhibition.getGuides());
			res.setRoom(oldExhibition.getRoom());

		}

		//Validate the entity, with the prefix in the ticker and keep the errors in an Error object
		final Errors allErrors = new BeanPropertyBindingResult(res, binding.getObjectName());
		this.validator.validate(res, allErrors);

		if (prunedExhibition.getId() == 0)
			res.setTicker(prunedExhibition.getTicker());

		//If it had error in the ticker field, we must remove the prefix from the ticker
		if (allErrors.hasFieldErrors("ticker")) {

			final FieldError tickerErrors = allErrors.getFieldError("ticker");
			final List<ObjectError> errors = allErrors.getAllErrors();

			//Add all errors except for the ticker
			for (final ObjectError oe : errors)
				if (!oe.equals(tickerErrors))
					binding.addError(oe);

			//Re-set the ticker to the original
			res.setTicker(res.getTicker().replaceFirst(tickerPrefix, ""));

			//Add the binding the error we previously had, but now the field value is restored
			binding.rejectValue("ticker", tickerErrors.getCode(), tickerErrors.getArguments(), tickerErrors.getDefaultMessage());
		} else
			//If not we simply add them all
			binding.addAllErrors(allErrors);

		return res;
	}

	// v3.0 - Alicia
	public Exhibition reconstructSave(final ExhibitionForm exhibitionForm, final BindingResult binding) {
		Assert.notNull(exhibitionForm);

		final Exhibition res = this.create();

		final Exhibition oldExhibition = this.findOne(exhibitionForm.getExhibition().getId());

		res.setId(oldExhibition.getId());
		res.setVersion(oldExhibition.getVersion());

		res.setTicker(oldExhibition.getTicker());
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
		res.setArtworks(oldExhibition.getArtworks());

		final Collection<Guide> newGuides = new HashSet<Guide>();
		newGuides.addAll(oldExhibition.getGuides());

		if (exhibitionForm.getGuides() != null)
			for (final String s : exhibitionForm.getGuides()) {
				final Guide g = this.guideService.findOne(new Integer(s));
				Assert.notNull(g);
				Assert.isTrue(oldExhibition.getRoom().getMuseum().getGuides().contains(g));
				Assert.isTrue(!oldExhibition.getGuides().contains(g));
				newGuides.add(g);
				g.getExhibitions().add(res);
				this.guideService.save(g);
			}

		res.setGuides(newGuides);

		res.setRoom(oldExhibition.getRoom());

		this.validator.validate(res, binding);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getCurrentByRoom(final Room room) {
		Assert.notNull(room);

		final Collection<Exhibition> res = this.exhibitionRepository.findCurrentByRoomId(room.getId());

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getByRoom(final Room room) {
		Assert.notNull(room);

		final Collection<Exhibition> res = this.exhibitionRepository.findByRoomId(room.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getByRoom(final Room room, final int page, final int size) {
		Assert.notNull(room);

		final Page<Exhibition> res = this.exhibitionRepository.findByRoomId(room.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<String> getTickersByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<String> res = this.exhibitionRepository.findTickersByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Date> getStartingDatesByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Date> res = this.exhibitionRepository.findStartingDatesByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Date> getEndingDatesByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Date> res = this.exhibitionRepository.findEndingDatesByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<String> getRoomNamesByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<String> res = this.exhibitionRepository.findRoomNamesByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getFutureExhibitionsWithDayPassesByRoom(final Room room) {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Exhibition> res = this.exhibitionRepository.findFutureExhibitionsWithDayPassesByRoomId(room.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getFutureExhibitionsWithSponsorshipsByRoom(final Room room) {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Exhibition> res = this.exhibitionRepository.findFutureExhibitionsWithSponsorshipsByRoomId(room.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Boolean canBeSponsored(final Exhibition exhibition) {
		Boolean res = false;

		final Sponsor sponsor = this.sponsorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sponsor);

		if (exhibition.getStartingDate().after(new Date()))
			res = true;

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getExhibitionsByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Exhibition> res = this.exhibitionRepository.findExhibitionsByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}
}
