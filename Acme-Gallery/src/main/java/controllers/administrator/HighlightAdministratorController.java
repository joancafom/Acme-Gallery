/*
 * HighlightAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HighlightService;
import controllers.AbstractController;
import domain.Highlight;

@Controller
@RequestMapping("/highlight/administrator")
public class HighlightAdministratorController extends AbstractController {

	private final String		ACTOR_WS	= "administrator/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private HighlightService	highlightService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int highlightId) {
		final ModelAndView res;

		final Highlight highlight = this.highlightService.findOne(highlightId);
		Assert.notNull(highlight);
		Assert.isTrue(highlight.getIsFinal());

		res = new ModelAndView("highlight/display");

		res.addObject("highlight", highlight);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
