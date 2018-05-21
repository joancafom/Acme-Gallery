/*
 * GroupVisitorController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.AnnouncementService;
import services.CommentService;
import services.GroupService;
import services.VisitorService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Comment;
import domain.Group;
import domain.Visitor;

@Controller
@RequestMapping("/group/visitor")
public class GroupVisitorController extends AbstractController {

	private final String		ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private GroupService		groupService;

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private VisitorService		visitorService;

	@Autowired
	private CommentService		commentService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-148477-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		//A visitor can list all the open groups + the ones she or he is member of

		final Page<Group> pageResult = this.groupService.findAllOpenAndMember(page, 5);
		final Collection<Group> groups = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("group/list");

		res.addObject("groups", groups);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int groupId, final RedirectAttributes redirectAttributes) {

		final Group groupToDelete = this.groupService.findOne(groupId);
		Assert.notNull(groupToDelete);

		ModelAndView res;

		try {

			this.groupService.deleteOwner(groupToDelete);
			res = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:display.do?groupId=" + groupId);
			redirectAttributes.addFlashAttribute("message", "group.commit.error");
		}

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int groupId, @RequestParam(value = "d-4721629-p", defaultValue = "1") final Integer pageA, @RequestParam(value = "d-7822565-p", defaultValue = "1") final Integer pageP, @RequestParam(
		value = "d-1332617-p", defaultValue = "1") final Integer pageC, @RequestParam(value = "d-7822565-p", defaultValue = "1") final Integer pageR, @RequestParam(required = false) final Integer commentId) {

		final ModelAndView res;
		final Group group = this.groupService.findOne(groupId);
		Assert.notNull(group);

		//Retrieve the current Visitor
		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		//Only can display it if it is public. If it is closed, I must be either a member or the creator
		Assert.isTrue(!group.getIsClosed() || (group.getParticipants().contains(currentVisitor) || currentVisitor.getCreatedGroups().contains(group)));

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
		res.addObject("isMember", currentVisitor.getJoinedGroups().contains(group));
		res.addObject("own", currentVisitor.getCreatedGroups().contains(group));

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

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView joinGroup(final int groupId, final RedirectAttributes redirectAttributes) {

		final Group groupToJoin = this.groupService.findOne(groupId);
		Assert.notNull(groupToJoin);

		final ModelAndView res = new ModelAndView("redirect:display.do?groupId=" + groupId);

		try {
			this.groupService.joinPublicGroup(groupToJoin);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "group.commit.error");
		}

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/quit", method = RequestMethod.GET)
	public ModelAndView quitGroup(final int groupId, final RedirectAttributes redirectAttributes) {

		final Group groupToQuit = this.groupService.findOne(groupId);
		Assert.notNull(groupToQuit);

		ModelAndView res;

		try {

			this.groupService.quitGroup(groupToQuit);

			if (groupToQuit.getIsClosed())
				res = new ModelAndView("redirect:list.do");
			else
				res = new ModelAndView("redirect:display.do?groupId=" + groupId);
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:display.do?groupId=" + groupId);
			redirectAttributes.addFlashAttribute("message", "group.commit.error");
		}

		return res;
	}
}
