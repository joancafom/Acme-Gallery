/*
 * CritiqueDirectorController.java
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

import security.LoginService;
import services.AnnouncementService;
import services.CommentService;
import services.DirectorService;
import services.GroupService;
import services.MuseumService;
import services.VisitorService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Comment;
import domain.Director;
import domain.Group;
import domain.Museum;
import domain.Visitor;

@Controller
@RequestMapping("/group/director")
public class GroupDirectorController extends AbstractController {

	private final String		ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private MuseumService		museumService;

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private GroupService		groupService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private VisitorService		visitorService;

	@Autowired
	private AnnouncementService	announcementService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int museumId, @RequestParam(value = "d-148477-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		Assert.isTrue(museum.getDirector().equals(director));
		Assert.isTrue(director.getMuseums().contains(museum));

		final Page<Group> pageResult = this.groupService.findByMuseum(museum, page, 5);
		final Collection<Group> groups = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("group/list");
		res.addObject("groups", groups);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	/* v1.0 - ratbellido */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int groupId, @RequestParam(value = "d-4721629-p", defaultValue = "1") final Integer pageA, @RequestParam(value = "d-7822565-p", defaultValue = "1") final Integer pageP, @RequestParam(
		value = "d-1332617-p", defaultValue = "1") final Integer pageC, @RequestParam(value = "d-7822565-p", defaultValue = "1") final Integer pageR, @RequestParam(required = false) final Integer commentId) {
		final ModelAndView res;
		final Group group = this.groupService.findOne(groupId);
		Assert.notNull(group);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(group.getMuseum().getDirector().equals(director));

		final Page<Announcement> pageResultA = this.announcementService.findAllByGroup(pageA, 5, group);
		final Collection<Announcement> announcements = pageResultA.getContent();
		final Integer resultSizeAnnouncement = new Long(pageResultA.getTotalElements()).intValue();

		final Page<Visitor> pageResultP = this.visitorService.findAllByGroup(pageP, 5, group);
		final Collection<Visitor> participants = pageResultP.getContent();
		final Integer resultSizeParticipant = new Long(pageResultP.getTotalElements()).intValue();

		final Page<Comment> pageResultC = this.commentService.findAllRootByGroup(pageC, 5, group);
		final Collection<Comment> comments = pageResultC.getContent();
		final Integer resultSizeComment = new Long(pageResultC.getTotalElements()).intValue();

		res = new ModelAndView("group/display");

		res.addObject("group", group);
		res.addObject("announcements", announcements);
		res.addObject("resultSizeAnnouncements", resultSizeAnnouncement);
		res.addObject("participants", participants);
		res.addObject("resultSizeParticipants", resultSizeParticipant);
		res.addObject("comments", comments);
		res.addObject("resultSizeComments", resultSizeComment);

		if (commentId != null) {
			final Comment parentComment = this.commentService.findOne(commentId);
			Assert.notNull(parentComment);
			Assert.isTrue(parentComment.getGroup().equals(group));

			final Page<Comment> pageResultR = this.commentService.findRepliesByComment(pageC, 5, parentComment);
			final Collection<Comment> replies = pageResultR.getContent();
			final Integer resultSizeReplies = new Long(pageResultR.getTotalElements()).intValue();

			res.addObject("hasReplies", true);
			res.addObject("parentComment", parentComment);
			res.addObject("replies", replies);
			res.addObject("resultSizeReplies", resultSizeReplies);

		}

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
