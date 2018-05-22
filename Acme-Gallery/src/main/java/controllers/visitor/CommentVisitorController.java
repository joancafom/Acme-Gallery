/*
 * CommentVisitorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.GroupService;
import controllers.AbstractController;
import domain.Comment;
import domain.Group;

@Controller
@RequestMapping("/comment/visitor")
public class CommentVisitorController extends AbstractController {

	private final String	ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private CommentService	commentService;

	@Autowired
	private GroupService	groupService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false) final Integer groupId, @RequestParam(required = false) final Integer commentId) {
		final ModelAndView res;

		Assert.isTrue(groupId != null || commentId != null);
		Assert.isTrue(!(groupId != null && commentId != null));

		Comment comment = null;

		if (groupId != null) {

			final Group group = this.groupService.findOne(groupId);
			Assert.notNull(group);

			comment = this.commentService.create(group);

		} else if (commentId != null) {

			final Comment parentComment = this.commentService.findOne(commentId);
			Assert.notNull(parentComment);

			comment = this.commentService.create(parentComment);

		}

		res = this.createEditModelAndView(comment);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@RequestParam(required = false) final Integer groupId, @RequestParam(required = false) final Integer commentId, final Comment prunedComment, final BindingResult binding) {
		ModelAndView res = null;

		Assert.isTrue(groupId != null || commentId != null);
		Assert.isTrue(!(groupId != null && commentId != null));

		if (groupId != null) {
			final Group group = this.groupService.findOne(groupId);
			Assert.notNull(group);

			prunedComment.setGroup(group);

		} else if (commentId != null) {
			final Comment parentComment = this.commentService.findOne(commentId);
			Assert.notNull(parentComment);

			prunedComment.setParentComment(parentComment);
		}

		final Comment comment = this.commentService.reconstruct(prunedComment, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedComment);
		else
			try {
				final Comment commentS = this.commentService.saveCreate(comment);
				res = new ModelAndView("redirect:/group/visitor/display.do?groupId=" + commentS.getGroup().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(comment, "comment.commit.error");
			}

		return res;
	}

	// Ancillary Methods ------------------------------------------------------------------------------

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final Comment comment) {
		ModelAndView res;
		res = this.createEditModelAndView(comment, null);

		return res;
	}

	// v1.0 - Alicia
	private ModelAndView createEditModelAndView(final Comment comment, final String message) {
		final ModelAndView res;

		res = new ModelAndView("comment/edit");

		res.addObject("comment", comment);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
