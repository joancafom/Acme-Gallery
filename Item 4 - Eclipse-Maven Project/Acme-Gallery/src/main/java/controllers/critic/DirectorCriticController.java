/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.critic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DirectorService;
import services.MuseumService;
import controllers.AbstractController;
import domain.Director;
import domain.Museum;

@Controller
@RequestMapping("/director/critic")
public class DirectorCriticController extends AbstractController {

	private final String	ACTOR_WS	= "critic/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService	directorService;

	@Autowired
	private MuseumService	museumService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value = "d-3996330-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Director> pageResult = this.directorService.findAllPaginated(page, 5);
		final Collection<Director> directors = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("director/list");
		res.addObject("directors", directors);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int directorId, @RequestParam(value = "d-447220-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;
		final Director director = this.directorService.findOne(directorId);
		Assert.notNull(director);

		final Page<Museum> pageResult = this.museumService.findAllPaginatedByDirector(page, 5, director);
		final Collection<Museum> museums = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("director/display");
		res.addObject("director", director);
		res.addObject("museums", museums);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}
}
