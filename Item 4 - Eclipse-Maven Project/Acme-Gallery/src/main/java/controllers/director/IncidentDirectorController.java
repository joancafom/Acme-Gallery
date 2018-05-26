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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.DirectorService;
import services.IncidentService;
import services.MuseumService;
import controllers.AbstractController;
import domain.Director;
import domain.Incident;
import domain.Museum;

@Controller
@RequestMapping("/incident/director")
public class IncidentDirectorController extends AbstractController {

	private final String	ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private IncidentService	incidentService;

	@Autowired
	private DirectorService	directorService;

	@Autowired
	private MuseumService	museumService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(value = "d-4007480-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Page<Incident> pageResult = this.incidentService.getByDirector(director, page, 5);
		final Collection<Incident> incidents = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("incident/listMine");

		res.addObject("incidents", incidents);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int museumId, @RequestParam(value = "d-4007480-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		final Page<Incident> pageResult = this.incidentService.getByMuseum(museum, page, 5);
		final Collection<Incident> incidents = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("incident/list");

		res.addObject("incidents", incidents);
		res.addObject("resultSize", resultSize);
		res.addObject("museum", museum);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v2.0 - Alicia
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public ModelAndView check(@RequestParam final int incidentId, @RequestParam(required = false, defaultValue = "false") final Boolean listMine, final RedirectAttributes redirectAttributes) {
		ModelAndView res = null;

		final Incident incident = this.incidentService.findOne(incidentId);
		Assert.notNull(incident);

		if (listMine.equals(true))
			res = new ModelAndView("redirect:listMine.do");
		else if (listMine.equals(false))
			res = new ModelAndView("redirect:list.do?museumId=" + incident.getRoom().getMuseum().getId());

		try {
			this.incidentService.check(incident);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "incident.commit.error");
		}

		return res;
	}

	// v2.0 - Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int incidentId, @RequestParam(required = false, defaultValue = "false") final Boolean listMine, final RedirectAttributes redirectAttributes) {
		ModelAndView res = null;

		final Incident incident = this.incidentService.findOne(incidentId);
		Assert.notNull(incident);

		if (listMine.equals(true))
			res = new ModelAndView("redirect:listMine.do");
		else if (listMine.equals(false))
			res = new ModelAndView("redirect:list.do?museumId=" + incident.getRoom().getMuseum().getId());

		try {
			this.incidentService.delete(incident);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "incident.commit.error");
		}

		return res;
	}

	// Ancillary Methods ------------------------------------------------------------------------------

}
