/*
 * MuseumDirectorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.director;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.DirectorService;
import services.ExhibitionService;
import services.GuideService;
import services.MuseumService;
import services.ReviewService;
import controllers.AbstractController;
import domain.Director;
import domain.Exhibition;
import domain.Guide;
import domain.Museum;
import domain.Review;
import forms.MuseumForm;

@Controller
@RequestMapping("/museum/director")
public class MuseumDirectorController extends AbstractController {

	private final String		ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private MuseumService		museumService;

	@Autowired
	private ReviewService		reviewService;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private GuideService		guideService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - JA
	@RequestMapping(value = "/addGuide", method = RequestMethod.GET)
	public ModelAndView addGuide(@RequestParam final int museumId) {
		final ModelAndView res;

		final MuseumForm museumForm = new MuseumForm();

		final Museum museum = this.museumService.findOne(museumId);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(museum);
		Assert.notNull(director);

		Assert.isTrue(director.getMuseums().contains(museum));

		museumForm.setMuseum(museum);

		res = this.addGuideModelAndView(museumForm);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/addGuide", method = RequestMethod.POST, params = "save")
	public ModelAndView addGuide(final MuseumForm museumForm, final BindingResult binding) {
		ModelAndView res = null;

		final Museum museum = this.museumService.reconstructAddGuide(museumForm, binding);

		if (binding.hasErrors())
			res = this.addGuideModelAndView(museumForm);
		else
			try {
				this.museumService.saveAddGuide(museum);
				res = new ModelAndView("redirect:listMine.do");
			} catch (final Throwable oops) {
				res = this.addGuideModelAndView(museumForm, "museum.commit.error");
			}

		return res;

	}
	// v1.0 - JA
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;

		final Museum museum = this.museumService.create();

		res = this.createEditModelAndView(museum);
		return res;
	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int museumId, @RequestParam(value = "d-447218-p", defaultValue = "1") final Integer pageR, @RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer pageE) {

		final ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		//Reviews are also listed in a Museum's Profile
		final Page<Review> pageResultR = this.reviewService.findAllByMuseum(museum, pageR, 5);
		final Collection<Review> reviews = pageResultR.getContent();
		final Integer resultSizeR = new Long(pageResultR.getTotalElements()).intValue();

		//Exhibition are also listed in a Museum's Profile (in this case, all of them)
		final Page<Exhibition> pageResultE = this.exhibitionService.getAllByMuseum(museum, pageE, 5);
		final Collection<Exhibition> exhibitions = pageResultE.getContent();
		final Integer resultSizeE = new Long(pageResultE.getTotalElements()).intValue();

		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		res = new ModelAndView("museum/display");
		res.addObject("museum", museum);
		res.addObject("reviews", reviews);
		res.addObject("resultSizeR", resultSizeR);
		res.addObject("exhibitions", exhibitions);
		res.addObject("resultSizeE", resultSizeE);
		res.addObject("own", museum.getDirector().equals(currentDirector));
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int museumId) {
		ModelAndView res = null;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		Assert.isTrue(currentDirector.equals(museum.getDirector()));

		res = this.createEditModelAndView(museum);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Museum prunedMuseum, final BindingResult binding) {
		ModelAndView res = null;

		final Museum museum = this.museumService.reconstructEdit(prunedMuseum, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedMuseum);
		else
			try {
				final Museum savedMuseum;

				if (museum.getId() == 0)
					savedMuseum = this.museumService.saveCreate(museum);
				else
					savedMuseum = this.museumService.saveEdit(museum);

				res = new ModelAndView("redirect:display.do?museumId=" + savedMuseum.getId());
			} catch (final DataIntegrityViolationException oops) {
				res = this.createEditModelAndView(prunedMuseum, "museum.identifier.error");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedMuseum, "museum.commit.error");
			}

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-447220-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Page<Museum> pageResult = this.museumService.findAllPaginated(page, 5);
		final Collection<Museum> museums = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("museum/list");

		res.addObject("museums", museums);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "list");
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(value = "d-447220-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		final Page<Museum> pageResult = this.museumService.findAllPaginatedByDirector(page, 5, currentDirector);
		final Collection<Museum> museums = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("museum/list");

		res.addObject("museums", museums);
		res.addObject("resultSize", resultSize);
		res.addObject("own", true);
		res.addObject("landing", "listMine");
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	//Ancillary Methods

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Museum museum) {

		return this.createEditModelAndView(museum, null);
	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Museum museum, final String message) {

		final ModelAndView res;

		Assert.notNull(museum);

		res = new ModelAndView("museum/edit");
		res.addObject("museum", museum);
		res.addObject("message", message);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	protected ModelAndView addGuideModelAndView(final MuseumForm museumForm) {

		ModelAndView res;
		res = this.addGuideModelAndView(museumForm, null);

		return res;
	}

	// v1.0 - JA
	private ModelAndView addGuideModelAndView(final MuseumForm museumForm, final String message) {
		final ModelAndView res;

		res = new ModelAndView("museum/addGuide");

		final Collection<Guide> guides = this.guideService.getNotWorkingInMuseum(museumForm.getMuseum());

		res.addObject("museum", museumForm);
		res.addObject("guides", guides);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
