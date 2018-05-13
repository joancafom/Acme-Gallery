/*
 * ExhibitionAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ExhibitionService;
import controllers.AbstractController;
import domain.Exhibition;

@Controller
@RequestMapping("/exhibition/administrator")
public class ExhibitionAdministratorController extends AbstractController {

	private final String		ACTOR_WS	= "administrator/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ExhibitionService	exhibitionService;


	// Methods ----------------------------------------------------------------------------------------
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
	public ModelAndView listResults(@RequestParam final String keyword) {
		ModelAndView res;

		final Collection<Exhibition> exhibitions = this.exhibitionService.getByKeyword(keyword);

		res = new ModelAndView("exhibition/listResults");

		res.addObject("exhibitions", exhibitions);
		res.addObject("keyword", keyword);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
