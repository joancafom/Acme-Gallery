/*
 * IncidentGuideController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.guide;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.GuideService;
import services.IncidentService;
import services.MuseumService;
import services.RoomService;
import controllers.AbstractController;
import domain.Guide;
import domain.Incident;
import domain.Museum;
import domain.Room;

@Controller
@RequestMapping("/incident/guide")
public class IncidentGuideController extends AbstractController {

	private final String	ACTOR_WS	= "guide/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private IncidentService	incidentService;

	@Autowired
	private GuideService	guideService;

	@Autowired
	private MuseumService	museumService;

	@Autowired
	private RoomService		roomService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - JA
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int museumId) {
		ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		final Incident incident = this.incidentService.create();

		final Collection<Room> rooms = this.roomService.getByMuseum(museum);

		res = this.createEditModelAndView(incident);
		res.addObject("rooms", rooms);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Incident prunedIncident, final BindingResult binding) {

		//final Incident incidentToSave = this.incidentService.reconstructCreate(prunedIncident, binding);

		ModelAndView res = null;

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedIncident);
		else
			try {
				//this.incidentService.saveCreate(incidentToSave);
				//res = new ModelAndView("redirect:list.do?museumId=" + incidentToSave.getRoom().getMuseum().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedIncident, "incident.commit.error");
			}

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int museumId, @RequestParam(value = "d-4007480-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(guide);

		Assert.isTrue(museum.getGuides().contains(guide));

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

	//Ancillary Methods

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Incident incident) {

		return this.createEditModelAndView(incident, null);
	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Incident incident, final String message) {

		final ModelAndView res;

		Assert.notNull(incident);

		res = new ModelAndView("incident/edit");
		res.addObject("incident", incident);
		res.addObject("message", message);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
