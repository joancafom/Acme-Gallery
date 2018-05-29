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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

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

	// v2.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int exhibitionId) {
		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		Assert.isTrue(this.exhibitionService.canBeSponsored(exhibition));

		final Sponsorship sponsorship = this.sponsorshipService.create(exhibition);

		res = this.createEditModelAndView(sponsorship);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int sponsorshipId) {
		final ModelAndView res;

		final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		final Sponsor sponsor = this.sponsorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sponsorship);
		Assert.notNull(sponsor);

		Assert.isTrue(sponsorship.getSponsor().equals(sponsor));
		Assert.isTrue(sponsorship.getStatus().equals("TIME_NEGOTIATION"));
		Assert.isTrue(sponsorship.getStartingDate().after(new Date()));

		res = this.acceptModelAndView(sponsorship);

		return res;

	}

	// v3.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-8273212-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Sponsor sponsor = this.sponsorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sponsor);

		final Page<Sponsorship> pageResult = this.sponsorshipService.getBySponsorPrincipal(page, 5);
		final Collection<Sponsorship> sponsorships = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("sponsorship/listMine");

		final Timestamp currentDate = new Timestamp(System.currentTimeMillis());

		res.addObject("sponsorships", sponsorships);
		res.addObject("resultSize", resultSize);
		res.addObject("currentDate", currentDate);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int sponsorshipId, final RedirectAttributes redirectAttributes) {
		final ModelAndView res;

		res = new ModelAndView("redirect:listMine.do");

		final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);

		try {
			this.sponsorshipService.reject(sponsorship);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "sponsorship.commit.error");
		}

		return res;
	}

	// v3.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@RequestParam final int exhibitionId, final Sponsorship prunedSponsorship, final BindingResult binding) {
		ModelAndView res = null;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		Assert.isTrue(this.exhibitionService.canBeSponsored(exhibition));

		prunedSponsorship.setExhibition(exhibition);

		final Sponsorship sponsorship = this.sponsorshipService.reconstructCreate(prunedSponsorship, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedSponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				res = new ModelAndView("redirect:listMine.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedSponsorship, "sponsorship.commit.error");
			}

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/accept", method = RequestMethod.POST, params = "save")
	public ModelAndView accept(final Sponsorship prunedSponsorship, final BindingResult binding) {
		ModelAndView res = null;

		final Sponsorship sponsorship = this.sponsorshipService.reconstructAccept(prunedSponsorship, binding);

		if (binding.hasErrors())
			res = this.acceptModelAndView(prunedSponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				res = new ModelAndView("redirect:listMine.do");
			} catch (final Throwable oops) {
				res = this.acceptModelAndView(prunedSponsorship, "sponsorship.commit.error");
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
	protected ModelAndView acceptModelAndView(final Sponsorship sponsorship) {
		ModelAndView res;
		res = this.acceptModelAndView(sponsorship, null);

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

	// v1.0 - Alicia
	protected ModelAndView acceptModelAndView(final Sponsorship sponsorship, final String message) {
		final ModelAndView res;

		Assert.notNull(sponsorship);

		res = new ModelAndView("sponsorship/accept");

		res.addObject("sponsorship", sponsorship);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
