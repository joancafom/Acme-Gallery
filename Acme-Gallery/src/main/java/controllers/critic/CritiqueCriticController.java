/*
 * CritiqueCriticController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.critic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CritiqueService;
import controllers.AbstractController;
import domain.Critique;

@Controller
@RequestMapping("/critique/critic")
public class CritiqueCriticController extends AbstractController {

	private final String	ACTOR_WS	= "critic/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private CritiqueService	critiqueService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int critiqueId) {
		final ModelAndView res;

		final Critique critique = this.critiqueService.findOne(critiqueId);
		Assert.notNull(critique);

		res = new ModelAndView("critique/display");

		res.addObject("critique", critique);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
