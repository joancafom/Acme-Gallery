/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.visitor;

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
import services.AnnouncementService;
import services.GroupService;
import services.VisitorService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Group;
import domain.Visitor;

@Controller
@RequestMapping("/announcement/visitor")
public class AnnouncementVisitorController extends AbstractController {

	private final String		ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private GroupService		groupService;

	@Autowired
	private VisitorService		visitorService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int groupId) {
		final ModelAndView res;
		final Group group = this.groupService.findOne(groupId);
		Assert.notNull(group);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		Assert.isTrue(group.getCreator().equals(visitor));

		final Announcement announcement = this.announcementService.create(group);

		res = this.createEditModelAndView(announcement);

		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public ModelAndView stream(@RequestParam(value = "d-4721629-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Announcement> pageResult = this.announcementService.getStreamByPrincipal(page, 5);
		final Collection<Announcement> announcements = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("announcement/stream");

		res.addObject("announcements", announcements);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Announcement prunedAnnouncement, final BindingResult binding) {
		ModelAndView res = null;
		final Announcement announcement = this.announcementService.reconstruct(prunedAnnouncement, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedAnnouncement);
		else
			try {
				final Announcement saved = this.announcementService.saveCreate(announcement);
				res = new ModelAndView("redirect:/group/visitor/display.do?groupId=" + saved.getGroup().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(announcement, "announcement.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Announcement announcement) {
		return this.createEditModelAndView(announcement, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Announcement announcement, final String message) {
		ModelAndView result;
		result = new ModelAndView("announcement/edit");
		result.addObject("announcement", announcement);
		result.addObject("message", message);
		result.addObject("group", announcement.getGroup());
		return result;
	}
}
