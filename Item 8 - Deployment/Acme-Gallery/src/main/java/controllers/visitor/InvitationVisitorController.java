/*
 * InvitationVisitorController.java
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.GroupService;
import services.InvitationService;
import services.VisitorService;
import controllers.AbstractController;
import domain.Group;
import domain.Invitation;
import domain.Visitor;

@Controller
@RequestMapping("/invitation/visitor")
public class InvitationVisitorController extends AbstractController {

	private final String		ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private InvitationService	invitationService;

	@Autowired
	private GroupService		groupService;

	@Autowired
	private VisitorService		visitorService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/process", method = RequestMethod.POST, params = "accept")
	public ModelAndView accept(final int invitationId, final RedirectAttributes redirectAttributes) {

		ModelAndView res;

		final Invitation invitation = this.invitationService.findOne(invitationId);
		Assert.notNull(invitation);

		try {
			this.invitationService.process(invitation, true);
			res = new ModelAndView("redirect:/group/visitor/display.do?groupId=" + invitation.getGroup().getId());
		} catch (final RuntimeException oops) {
			res = new ModelAndView("redirect:display.do?invitationId=" + invitationId);
			redirectAttributes.addFlashAttribute("message", "invitation.max.error");
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:display.do?invitationId=" + invitationId);
			redirectAttributes.addFlashAttribute("message", "invitation.commit.error");
		}

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int groupId) {

		final ModelAndView res;

		final Group group = this.groupService.findOne(groupId);
		Assert.notNull(group);

		final Invitation invitation = this.invitationService.create(group);

		res = this.createEditModelAndView(invitation);

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final int invitationId) {

		final ModelAndView res;

		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		final Invitation invitation = this.invitationService.findOne(invitationId);
		Assert.notNull(invitation);

		Assert.isTrue(currentVisitor.equals(invitation.getGuest()));

		res = new ModelAndView("invitation/display");
		res.addObject("invitation", invitation);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Invitation prunedInvitation, final BindingResult binding) {

		final Invitation invitationToSend = this.invitationService.reconstructCreate(prunedInvitation, binding);
		prunedInvitation.setGroup(invitationToSend.getGroup());

		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedInvitation);
		else
			try {
				this.invitationService.sendInvitation(invitationToSend);
				res = new ModelAndView("redirect:/group/visitor/display.do?groupId=" + invitationToSend.getGroup().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedInvitation, "invitation.commit.error");
			}

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-2558511-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		final Page<Invitation> pageResult = this.invitationService.findAllReceivedByVisitor(currentVisitor, page, 5);
		final Collection<Invitation> invitations = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("invitation/list");

		res.addObject("invitations", invitations);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/process", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(final int invitationId, final RedirectAttributes redirectAttributes) {

		ModelAndView res;

		final Invitation invitation = this.invitationService.findOne(invitationId);
		Assert.notNull(invitation);

		try {
			this.invitationService.process(invitation, false);
			res = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:display.do?invitationId=" + invitationId);
			redirectAttributes.addFlashAttribute("message", "invitation.commit.error");
		}

		return res;
	}

	//Ancillary Methods

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Invitation invitation) {

		return this.createEditModelAndView(invitation, null);
	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Invitation invitation, final String message) {

		final ModelAndView res;

		Assert.notNull(invitation);

		Assert.notNull(invitation.getGroup());
		final Collection<Visitor> remainingMembers = this.visitorService.findRemainingByGroup(invitation.getGroup());

		res = new ModelAndView("invitation/edit");
		res.addObject("invitation", invitation);
		res.addObject("message", message);
		res.addObject("remainingMembers", remainingMembers);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
