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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.GroupService;
import services.InvitationService;
import services.VisitorService;
import controllers.AbstractController;
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

}
