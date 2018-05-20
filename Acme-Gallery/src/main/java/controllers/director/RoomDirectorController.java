/*
 * RoomDirectorController.java
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
import services.ExhibitionService;
import services.IncidentService;
import services.MuseumService;
import services.RoomService;
import controllers.AbstractController;
import domain.Director;
import domain.Exhibition;
import domain.Incident;
import domain.Museum;
import domain.Room;

@Controller
@RequestMapping("/room/director")
public class RoomDirectorController extends AbstractController {

	private final String		ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private IncidentService		incidentService;

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private MuseumService		museumService;

	@Autowired
	private RoomService			roomService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int roomId) {
		final ModelAndView res;

		final Room room = this.roomService.findOne(roomId);
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(room.getMuseum().getDirector().equals(director));

		final Collection<Exhibition> currentExhibition = this.exhibitionService.getCurrentByRoom(room);
		final Collection<Exhibition> allExhibitions = this.exhibitionService.getByRoom(room);
		final Collection<Incident> incidents = this.incidentService.getByRoom(room);

		final Boolean canBeMarkedAsInRepair = this.roomService.canBeMarkedAsInRepair(room);
		final Boolean canBeMarkedAsNotInRepair = this.roomService.canBeMarkedAsNotInRepair(room);

		res = new ModelAndView("room/display");
		res.addObject("room", room);
		res.addObject("currentExhibition", currentExhibition);
		res.addObject("allExhibitions", allExhibitions);
		res.addObject("incidents", incidents);
		res.addObject("canBeMarkedAsInRepair", canBeMarkedAsInRepair);
		res.addObject("canBeMarkedAsNotInRepair", canBeMarkedAsNotInRepair);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
	// v1.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(value = "d-49717-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Page<Room> pageResult = this.roomService.getByDirector(director, page, 5);
		final Collection<Room> rooms = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("room/listMine");

		res.addObject("rooms", rooms);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int museumId, @RequestParam(value = "d-49717-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		final Page<Room> pageResult = this.roomService.getByMuseum(museum, page, 5);
		final Collection<Room> rooms = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("room/list");

		res.addObject("rooms", rooms);
		res.addObject("resultSize", resultSize);
		res.addObject("museum", museum);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/inRepair", method = RequestMethod.GET)
	public ModelAndView inRepair(@RequestParam final int roomId, final RedirectAttributes redirectAttributes) {
		final ModelAndView res;

		final Room room = this.roomService.findOne(roomId);
		Assert.notNull(room);

		res = new ModelAndView("redirect:display.do?roomId=" + room.getId());

		try {
			this.roomService.inRepair(room);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "room.commit.error");
		}

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/notInRepair", method = RequestMethod.GET)
	public ModelAndView notInRepair(@RequestParam final int roomId, final RedirectAttributes redirectAttributes) {
		final ModelAndView res;

		final Room room = this.roomService.findOne(roomId);
		Assert.notNull(room);

		res = new ModelAndView("redirect:display.do?roomId=" + room.getId());

		try {
			this.roomService.notInRepair(room);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "room.commit.error");
		}

		return res;
	}

	// Ancillary Methods ------------------------------------------------------------------------------

}
