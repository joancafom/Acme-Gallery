/*
 * ExhibitionDirectorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.director;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.ArtworkService;
import services.CategoryService;
import services.CritiqueService;
import services.DirectorService;
import services.ExhibitionService;
import services.GuideService;
import services.RoomService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Artwork;
import domain.Category;
import domain.Critique;
import domain.Director;
import domain.Exhibition;
import domain.Guide;
import domain.Room;
import domain.Sponsorship;
import forms.ExhibitionForm;

@Controller
@RequestMapping("/exhibition/director")
public class ExhibitionDirectorController extends AbstractController {

	private final String		ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private ArtworkService		artworkService;

	@Autowired
	private CritiqueService		critiqueService;

	@Autowired
	private GuideService		guideService;

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private RoomService			roomService;

	@Autowired
	private SponsorshipService	sponsorshipService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;
		final Exhibition exhibition = this.exhibitionService.create();

		res = this.createModelAndView(exhibition);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int exhibitionId) {
		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(exhibition);
		Assert.notNull(director);

		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));
		Assert.isTrue(exhibition.getDayPasses().isEmpty());

		res = this.editModelAndView(exhibition);

		return res;

	}

	// v1.0 - Alicia
	@RequestMapping(value = "/editDetails", method = RequestMethod.GET)
	public ModelAndView editDetails(@RequestParam final int exhibitionId) {
		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(exhibition);
		Assert.notNull(director);

		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));
		Assert.isTrue(!exhibition.getDayPasses().isEmpty());

		res = this.editDetailsModelAndView(exhibition);

		return res;

	}

	// v1.0 - Alicia
	@RequestMapping(value = "/addGuide", method = RequestMethod.GET)
	public ModelAndView addGuide(@RequestParam final int exhibitionId) {
		final ModelAndView res;

		final ExhibitionForm exhibitionForm = new ExhibitionForm();

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(exhibition);
		Assert.notNull(director);

		Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));

		exhibitionForm.setExhibition(exhibition);

		res = this.addGuideModelAndView(exhibitionForm);

		return res;

	}

	//v1.0 - Implemented by JA
	// v2.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int exhibitionId, @RequestParam(value = "d-1332818-p", defaultValue = "1") final Integer pageA, @RequestParam(value = "d-3999872-p", defaultValue = "1") final Integer pageC, @RequestParam(
		value = "d-148442-p", defaultValue = "1") final Integer pageG) {

		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		//Artworks are also listed in an Exhibition Profile
		final Page<Artwork> pageResultA = this.artworkService.findAllByExhibition(exhibition, pageA, 5);
		final Collection<Artwork> artworks = pageResultA.getContent();
		final Integer resultSizeA = new Long(pageResultA.getTotalElements()).intValue();

		//Critiques are also listed in an Exhibition Profile
		final Page<Critique> pageResultC = this.critiqueService.findAllByExhibition(exhibition, pageC, 5);
		final Collection<Critique> critiques = pageResultC.getContent();
		final Integer resultSizeC = new Long(pageResultC.getTotalElements()).intValue();

		//Guides are also listed in an Exhibition Profile
		final Page<Guide> pageResultG = this.guideService.findAllByExhibition(exhibition, pageG, 5);
		final Collection<Guide> guides = pageResultG.getContent();
		final Integer resultSizeG = new Long(pageResultG.getTotalElements()).intValue();

		Boolean canBeDeleted = false;

		if (director.getMuseums().contains(exhibition.getRoom().getMuseum()) && exhibition.getStartingDate().after(new Date()) && exhibition.getDayPasses().isEmpty() && exhibition.getSponsorships().isEmpty())
			canBeDeleted = true;

		//We must display the current sponsorship, if any
		final Sponsorship currentSponsorship = this.sponsorshipService.findCurrentByExhibition(exhibition);

		res = new ModelAndView("exhibition/display");
		res.addObject("exhibition", exhibition);
		res.addObject("artworks", artworks);
		res.addObject("resultSizeA", resultSizeA);
		res.addObject("critiques", critiques);
		res.addObject("resultSizeC", resultSizeC);
		res.addObject("guides", guides);
		res.addObject("resultSizeG", resultSizeG);
		res.addObject("canBeDeleted", canBeDeleted);
		res.addObject("ad", currentSponsorship);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;

		res = new ModelAndView("exhibition/search");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int exhibitionId, final RedirectAttributes redirectAttributes) {

		final ModelAndView res;
		res = new ModelAndView("redirect:listMine.do");

		final Exhibition toDelete = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(toDelete);

		try {
			this.exhibitionService.delete(toDelete);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "exhibition.commit.error");
		}

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/listResults", method = RequestMethod.GET)
	public ModelAndView listResults(@RequestParam final String keyword, @RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Page<Exhibition> pageResult = this.exhibitionService.getByKeyword(keyword, page, 5);
		final Collection<Exhibition> exhibitions = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("exhibition/listResults");

		res.addObject("exhibitions", exhibitions);
		res.addObject("resultSize", resultSize);
		res.addObject("keyword", keyword);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Page<Exhibition> pageResult = this.exhibitionService.getByDirector(director, page, 5);
		final Collection<Exhibition> exhibitions = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("exhibition/listMine");

		res.addObject("exhibitions", exhibitions);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v2.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Exhibition prunedExhibition, final BindingResult binding) {
		ModelAndView res = null;

		if (prunedExhibition.getId() != 0) {
			final Exhibition oldExhibition = this.exhibitionService.findOne(prunedExhibition.getId());
			Assert.notNull(oldExhibition);
			Assert.isTrue(oldExhibition.getDayPasses().isEmpty());
		}

		final Exhibition exhibition = this.exhibitionService.reconstructSave(prunedExhibition, binding);

		if (binding.hasErrors())
			res = this.editModelAndView(prunedExhibition);
		else
			try {
				final Exhibition exhibitionS = this.exhibitionService.saveCreateAndEdit(exhibition);
				res = new ModelAndView("redirect:/exhibition/director/display.do?exhibitionId=" + exhibitionS.getId());
			} catch (final Throwable oops) {
				res = this.editModelAndView(prunedExhibition, "exhibition.commit.error");
			}

		return res;

	}

	// v2.0 - Alicia
	@RequestMapping(value = "/editDetails", method = RequestMethod.POST, params = "save")
	public ModelAndView editDetails(final Exhibition prunedExhibition, final BindingResult binding) {
		ModelAndView res = null;

		final Exhibition oldExhibition = this.exhibitionService.findOne(prunedExhibition.getId());
		Assert.isTrue(!oldExhibition.getDayPasses().isEmpty());

		final Exhibition exhibition = this.exhibitionService.reconstructSave(prunedExhibition, binding);

		if (binding.hasErrors())
			res = this.editDetailsModelAndView(prunedExhibition);
		else
			try {
				final Exhibition exhibitionS = this.exhibitionService.saveCreateAndEdit(exhibition);
				res = new ModelAndView("redirect:/exhibition/director/display.do?exhibitionId=" + exhibitionS.getId());
			} catch (final Throwable oops) {
				res = this.editDetailsModelAndView(prunedExhibition, "exhibition.commit.error");
			}

		return res;

	}

	// v1.0 - Alicia
	@RequestMapping(value = "/addGuide", method = RequestMethod.POST, params = "save")
	public ModelAndView addGuide(final ExhibitionForm exhibitionForm, final BindingResult binding) {
		ModelAndView res = null;

		final Exhibition exhibition = this.exhibitionService.reconstructSave(exhibitionForm, binding);

		if (binding.hasErrors())
			res = this.addGuideModelAndView(exhibitionForm);
		else
			try {
				final Exhibition exhibitionS = this.exhibitionService.saveCreateAndEdit(exhibition);
				res = new ModelAndView("redirect:/exhibition/director/display.do?exhibitionId=" + exhibitionS.getId());
			} catch (final Throwable oops) {
				res = this.addGuideModelAndView(exhibitionForm, "exhibition.commit.error");
			}

		return res;

	}

	// Ancillary Methods ------------------------------------------------------------------------------

	// v1.0 - Alicia
	protected ModelAndView createModelAndView(final Exhibition exhibition) {
		ModelAndView res;
		res = this.createModelAndView(exhibition, null);

		return res;
	}

	// v1.0 - Alicia
	protected ModelAndView editModelAndView(final Exhibition exhibition) {
		ModelAndView res;
		res = this.editModelAndView(exhibition, null);

		return res;
	}

	// v1.0 - Alicia
	protected ModelAndView editDetailsModelAndView(final Exhibition exhibition) {
		ModelAndView res;
		res = this.editDetailsModelAndView(exhibition, null);

		return res;
	}

	// v1.0 - Alicia
	protected ModelAndView addGuideModelAndView(final ExhibitionForm exhibition) {
		ModelAndView res;
		res = this.addGuideModelAndView(exhibition, null);

		return res;
	}

	// v1.0 - Alicia
	private ModelAndView createModelAndView(final Exhibition exhibition, final String message) {
		final ModelAndView res;

		res = new ModelAndView("exhibition/edit");

		final Collection<Category> categories = this.categoryService.getAllExceptRoot();
		final Collection<Room> rooms = this.roomService.getByPrincipal();
		final Collection<String> tickers = this.exhibitionService.getTickersByPrincipal();
		final Collection<Date> startingDates = this.exhibitionService.getStartingDatesByPrincipal();
		final Collection<Date> endingDates = this.exhibitionService.getEndingDatesByPrincipal();
		final Collection<String> roomNames = this.exhibitionService.getRoomNamesByPrincipal();
		final Integer exhSize = startingDates.size();

		res.addObject("tickers", tickers);
		res.addObject("startingDates", startingDates);
		res.addObject("endingDates", endingDates);
		res.addObject("roomNames", roomNames);
		res.addObject("exhSize", exhSize);
		res.addObject("exhibition", exhibition);
		res.addObject("categories", categories);
		res.addObject("rooms", rooms);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v3.0 - Alicia
	private ModelAndView editModelAndView(final Exhibition exhibition, final String message) {
		final ModelAndView res;

		res = new ModelAndView("exhibition/edit");

		final Collection<Category> categories = this.categoryService.getAllExceptRoot();
		final Collection<Room> rooms = this.roomService.getByPrincipal();
		final Collection<String> tickers = this.exhibitionService.getTickersByPrincipal();
		final Collection<Date> startingDates = this.exhibitionService.getStartingDatesByPrincipal();
		final Collection<Date> endingDates = this.exhibitionService.getEndingDatesByPrincipal();
		final Collection<String> roomNames = this.exhibitionService.getRoomNamesByPrincipal();
		final Integer exhSize = startingDates.size();

		res.addObject("tickers", tickers);
		res.addObject("startingDates", startingDates);
		res.addObject("endingDates", endingDates);
		res.addObject("roomNames", roomNames);
		res.addObject("exhSize", exhSize);
		res.addObject("exhibition", exhibition);
		res.addObject("categories", categories);
		res.addObject("rooms", rooms);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v3.0 - Alicia
	private ModelAndView editDetailsModelAndView(final Exhibition exhibition, final String message) {
		final ModelAndView res;

		res = new ModelAndView("exhibition/edit");

		final Collection<Category> categories = this.categoryService.getAllExceptRoot();

		res.addObject("exhibition", exhibition);
		res.addObject("categories", categories);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	private ModelAndView addGuideModelAndView(final ExhibitionForm exhibition, final String message) {
		final ModelAndView res;

		res = new ModelAndView("exhibition/addGuide");

		final Collection<Guide> guides = this.guideService.getNotWorkingInExhibition(exhibition.getExhibition());

		res.addObject("exhibition", exhibition);
		res.addObject("guides", guides);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
