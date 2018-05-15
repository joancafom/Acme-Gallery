/*
 * MuseumDirectorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.director;

import java.sql.Timestamp;
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
import services.DirectorService;
import services.ExhibitionService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Director;
import domain.Exhibition;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/director")
public class SponsorshipDirectorController extends AbstractController {

	//	private final String		ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private DirectorService		directorService;


	// Methods ----------------------------------------------------------------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int exhibitionId, @RequestParam(value = "d-8273212-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		//(More safety measurements are included in the inner method)

		//Sponsorships of this Exhibition
		final Page<Sponsorship> pageResult = this.sponsorshipService.findAllByExhibition(exhibition, page, 5);
		final Collection<Sponsorship> sponsorships = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		//Current Date to determine EXPIREd cases
		final Timestamp currentDate = new Timestamp(System.currentTimeMillis());

		res = new ModelAndView("sponsorship/list");
		res.addObject("sponsorships", sponsorships);
		res.addObject("resultSize", resultSize);
		res.addObject("exhibition", exhibition);
		res.addObject("currentDate", currentDate);

		return res;
	}
}
