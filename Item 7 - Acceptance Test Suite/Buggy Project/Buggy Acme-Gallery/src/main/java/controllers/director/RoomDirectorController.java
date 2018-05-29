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
import org.springframework.validation.BindingResult;
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
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;
		final Room room = this.roomService.create();

		res = this.createEditModelAndView(room);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int roomId, @RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer pageE, @RequestParam(value = "d-4007480-p", defaultValue = "1") final Integer pageI) {
		final ModelAndView res;

		final Room room = this.roomService.findOne(roomId);
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(room.getMuseum().getDirector().equals(director));

		final Collection<Exhibition> currentExhibition = this.exhibitionService.getCurrentByRoom(room);

		final Page<Exhibition> ePageResult = this.exhibitionService.getByRoom(room, pageE, 5);
		final Collection<Exhibition> allExhibitions = ePageResult.getContent();
		final Integer eResultSize = new Long(ePageResult.getTotalElements()).intValue();

		final Page<Incident> iPageResult = this.incidentService.getByRoom(room, pageI, 5);
		final Collection<Incident> incidents = iPageResult.getContent();
		final Integer iResultSize = new Long(iPageResult.getTotalElements()).intValue();

		final Boolean canBeMarkedAsInRepair = this.roomService.canBeMarkedAsInRepair(room);
		final Boolean canBeMarkedAsNotInRepair = this.roomService.canBeMarkedAsNotInRepair(room);
		final Boolean canBeDeleted = this.roomService.canBeDeleted(room);

		res = new ModelAndView("room/display");
		res.addObject("room", room);
		res.addObject("currentExhibition", currentExhibition);
		res.addObject("allExhibitions", allExhibitions);
		res.addObject("eResultSize", eResultSize);
		res.addObject("incidents", incidents);
		res.addObject("iResultSize", iResultSize);
		res.addObject("canBeMarkedAsInRepair", canBeMarkedAsInRepair);
		res.addObject("canBeMarkedAsNotInRepair", canBeMarkedAsNotInRepair);
		res.addObject("canBeDeleted", canBeDeleted);

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
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int roomId, final RedirectAttributes redirectAttributes) {
		final ModelAndView res;
		res = new ModelAndView("redirect:listMine.do");

		final Room toDelete = this.roomService.findOne(roomId);
		Assert.notNull(toDelete);

		try {
			this.roomService.delete(toDelete);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "room.commit.error");
		}

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Room prunedRoom, final BindingResult binding) {
		ModelAndView res = null;

		final Room room = this.roomService.reconstructSave(prunedRoom, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedRoom);
		else
			try {
				final Room roomS = this.roomService.saveCreateAndEdit(room);
				res = new ModelAndView("redirect:/room/director/display.do?roomId=" + roomS.getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(room, "room.commit.error");
			}

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

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final Room room) {
		ModelAndView res;
		res = this.createEditModelAndView(room, null);

		return res;
	}

	// v1.0 - Alicia
	private ModelAndView createEditModelAndView(final Room room, final String message) {
		final ModelAndView res;

		res = new ModelAndView("room/edit");

		final Collection<Museum> museums = this.museumService.getByPrincipal();

		res.addObject("room", room);
		res.addObject("museums", museums);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
