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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ExhibitionService;
import services.MuseumService;
import services.ReviewService;
import controllers.AbstractController;
import domain.Exhibition;
import domain.Museum;
import domain.Review;

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


	// Methods ----------------------------------------------------------------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int museumId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer pageR, @RequestParam(value = "d-1332309-p", defaultValue = "1") final Integer pageE) {

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

		res = new ModelAndView("museum/display");
		res.addObject("museum", museum);
		res.addObject("reviews", reviews);
		res.addObject("resultSizeR", resultSizeR);
		res.addObject("exhibitions", exhibitions);
		res.addObject("resultSizeE", resultSizeE);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
