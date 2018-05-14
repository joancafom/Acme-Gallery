/*
 * CategoryAdministratorController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import controllers.AbstractController;
import domain.Announcement;

@Controller
@RequestMapping("/announcement/administrator")
public class AnnouncementAdministratorController extends AbstractController {

	//private final String		ACTOR_WS	= "administrator/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listTabooed(@RequestParam(value = "d-5218124-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Announcement> pageResult = this.announcementService.findTabooed(page, 5);
		final Collection<Announcement> annoucements = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("announcement/list");

		res.addObject("announcements", annoucements);
		res.addObject("resultSize", resultSize);

		return res;
	}
}
