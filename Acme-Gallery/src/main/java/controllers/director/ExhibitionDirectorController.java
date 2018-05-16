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

import security.LoginService;
import services.CategoryService;
import services.CritiqueService;
import services.DirectorService;
import services.ExhibitionService;
import services.GuideService;
import services.HighlightService;
import services.RoomService;
import controllers.AbstractController;
import domain.Category;
import domain.Critique;
import domain.Director;
import domain.Exhibition;
import domain.Guide;
import domain.Highlight;
import domain.Room;

@Controller
@RequestMapping("/exhibition/director")
public class ExhibitionDirectorController extends AbstractController {

	private final String		ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private HighlightService	highlightService;

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


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;
		final Exhibition exhibition = this.exhibitionService.create();

		res = this.createEditModelAndView(exhibition);

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

		res = this.createEditModelAndView(exhibition);

		return res;

	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int exhibitionId, @RequestParam(value = "d-3619782-p", defaultValue = "1") final Integer pageH, @RequestParam(value = "d-148442-p", defaultValue = "1") final Integer pageC, @RequestParam(
		value = "d-148442-p", defaultValue = "1") final Integer pageG) {

		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		//Highlights are also listed in an Exhibition Profile
		final Page<Highlight> pageResultH = this.highlightService.findAllByExhibition(exhibition, pageH, 5);
		final Collection<Highlight> highlights = pageResultH.getContent();
		final Integer resultSizeH = new Long(pageResultH.getTotalElements()).intValue();

		//Critiques are also listed in an Exhibition Profile
		final Page<Critique> pageResultC = this.critiqueService.findAllByExhibition(exhibition, pageC, 5);
		final Collection<Critique> critiques = pageResultC.getContent();
		final Integer resultSizeC = new Long(pageResultC.getTotalElements()).intValue();

		//Guides are also listed in an Exhibition Profile
		final Page<Guide> pageResultG = this.guideService.findAllByExhibition(exhibition, pageG, 5);
		final Collection<Guide> guides = pageResultG.getContent();
		final Integer resultSizeG = new Long(pageResultG.getTotalElements()).intValue();

		res = new ModelAndView("exhibition/display");
		res.addObject("exhibition", exhibition);
		res.addObject("highlights", highlights);
		res.addObject("resultSizeH", resultSizeH);
		res.addObject("critiques", critiques);
		res.addObject("resultSizeC", resultSizeC);
		res.addObject("guides", guides);
		res.addObject("resultSizeG", resultSizeG);
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

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Exhibition prunedExhibition, final BindingResult binding) {
		ModelAndView res = null;

		final Exhibition exhibition = this.exhibitionService.reconstructSave(prunedExhibition, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedExhibition);
		else
			try {
				final Exhibition exhibitionS = this.exhibitionService.saveCreateAndEdit(exhibition);
				res = new ModelAndView("redirect:/exhibition/director/display.do?exhibitionId=" + exhibitionS.getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(exhibition, "exhibition.commit.error");
			}

		return res;

	}

	// Ancillary Methods ------------------------------------------------------------------------------

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final Exhibition exhibition) {
		ModelAndView res;
		res = this.createEditModelAndView(exhibition, null);

		return res;
	}

	// v1.0 - Alicia
	private ModelAndView createEditModelAndView(final Exhibition exhibition, final String message) {
		final ModelAndView res;

		res = new ModelAndView("exhibition/edit");

		final Collection<Category> categories = this.categoryService.getAllExceptRoot();
		final Collection<Room> rooms = this.roomService.getAvailableByPrincipal();

		res.addObject("exhibition", exhibition);
		res.addObject("categories", categories);
		res.addObject("rooms", rooms);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
