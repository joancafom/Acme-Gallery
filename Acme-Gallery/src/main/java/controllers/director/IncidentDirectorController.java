/*
 * IncidentDirectorController.java
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
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.DirectorService;
import services.IncidentService;
import controllers.AbstractController;
import domain.Director;
import domain.Incident;

@Controller
@RequestMapping("/incident/director")
public class IncidentDirectorController extends AbstractController {

	private final String	ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private IncidentService	incidentService;

	@Autowired
	private DirectorService	directorService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine() {
		ModelAndView res;

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Incident> incidents = this.incidentService.getByDirector(director);

		res = new ModelAndView("incident/listMine");

		res.addObject("incidents", incidents);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// Ancillary Methods ------------------------------------------------------------------------------

}
