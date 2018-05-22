/*
 * SponsorshipSponsorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.sponsor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ExhibitionService;
import services.SponsorService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Exhibition;
import domain.Sponsor;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	private final String		ACTOR_WS	= "sponsor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private SponsorService		sponsorService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int exhibitionId) {
		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		final Sponsorship sponsorship = this.sponsorshipService.create(exhibition);

		res = this.createEditModelAndView(sponsorship);

		return res;
	}

	// v2.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;

		final Sponsor sponsor = this.sponsorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sponsor);

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.getBySponsorPrincipal();

		//final Page<DayPass> pageResult = this.dayPassService.getByPrincipal(page, 5);
		//final Collection<DayPass> dayPasses = pageResult.getContent();
		//final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("sponsorship/listMine");

		res.addObject("sponsorships", sponsorships);
		//res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@RequestParam final int exhibitionId, final Sponsorship prunedSponsorship, final BindingResult binding) {
		ModelAndView res = null;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		prunedSponsorship.setExhibition(exhibition);

		final Sponsorship sponsorship = this.sponsorshipService.reconstructCreate(prunedSponsorship, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedSponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				//res = new ModelAndView("redirect:list.do");
				res = new ModelAndView("redirect:/welcome.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedSponsorship, "sponsorship.commit.error");
			}

		return res;
	}
	//Ancillary Methods -------------------------------

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView res;
		res = this.createEditModelAndView(sponsorship, null);

		return res;
	}

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String message) {
		final ModelAndView res;

		Assert.notNull(sponsorship);

		res = new ModelAndView("sponsorship/edit");

		res.addObject("sponsorship", sponsorship);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
