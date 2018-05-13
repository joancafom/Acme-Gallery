/*
 * ExhibitionSponsorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.sponsor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ExhibitionService;
import controllers.AbstractController;
import domain.Exhibition;

@Controller
@RequestMapping("/exhibition/sponsor")
public class ExhibitionSponsorController extends AbstractController {

	private final String		ACTOR_WS	= "sponsor/";

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

}
