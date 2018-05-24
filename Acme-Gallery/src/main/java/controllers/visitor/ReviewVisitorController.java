/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.visitor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.DayPassService;
import services.MuseumService;
import services.ReviewService;
import services.VisitorService;
import controllers.AbstractController;
import domain.DayPass;
import domain.Museum;
import domain.Review;
import domain.Visitor;

@Controller
@RequestMapping("/review/visitor")
public class ReviewVisitorController extends AbstractController {

	private final String	ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ReviewService	reviewService;

	@Autowired
	private MuseumService	museumService;

	@Autowired
	private VisitorService	visitorService;

	@Autowired
	private DayPassService	dayPassService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int museumId) {
		final ModelAndView res;
		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Collection<DayPass> dayPasses = this.dayPassService.findAllByMuseumAndPrincipal(museum);
		Assert.isTrue(dayPasses.isEmpty() == false);

		final Review review = this.reviewService.create(museum);

		res = this.createEditModelAndView(review);

		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Review prunedReview, final int museumId, final BindingResult binding) {
		ModelAndView res = null;
		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		prunedReview.setMuseum(museum);

		final Review review = this.reviewService.reconstruct(prunedReview, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedReview);
		else
			try {
				final Review saved = this.reviewService.saveCreate(review);
				res = new ModelAndView("redirect:/museum/visitor/display.do?museumId=" + saved.getMuseum().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(review, "review.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Review review) {
		return this.createEditModelAndView(review, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Review review, final String message) {
		ModelAndView result;
		result = new ModelAndView("review/edit");
		result.addObject("review", review);
		result.addObject("message", message);
		result.addObject("museum", review.getMuseum());
		return result;
	}
}
