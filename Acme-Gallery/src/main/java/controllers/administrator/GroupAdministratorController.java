/*
 * GroupAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

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

import services.GroupService;
import controllers.AbstractController;
import domain.Group;

@Controller
@RequestMapping("/group/administrator")
public class GroupAdministratorController extends AbstractController {

	private final String	ACTOR_WS	= "administrator/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private GroupService	groupService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/listTaboo", method = RequestMethod.GET)
	public ModelAndView listTaboo(@RequestParam(value = "d-148477-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Group> pageResult = this.groupService.findTabooed(page, 5);
		final Collection<Group> groups = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("group/listTaboo");

		res.addObject("groups", groups);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int groupId, final RedirectAttributes redirectAttributes) {

		final ModelAndView res;
		res = new ModelAndView("redirect:listTaboo.do");

		final Group toDelete = this.groupService.findOne(groupId);
		Assert.notNull(toDelete);

		try {
			this.groupService.delete(toDelete);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "group.commit.error");
		}

		return res;
	}
}
