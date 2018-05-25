/*
 * ArtworkAdministratorController.java
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

import services.ArtworkService;
import controllers.AbstractController;
import domain.Artwork;

@Controller
@RequestMapping("/artwork/administrator")
public class ArtworkAdministratorController extends AbstractController {

	private final String	ACTOR_WS	= "administrator/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ArtworkService	artworkService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int artworkId) {
		final ModelAndView res;

		final Artwork artwork = this.artworkService.findOne(artworkId);
		Assert.notNull(artwork);

		res = new ModelAndView("artwork/display");

		res.addObject("artwork", artwork);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
