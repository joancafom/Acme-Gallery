/*
 * DayPassVisitorController.java
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

import security.LoginService;
import services.DayPassService;
import services.ExhibitionService;
import services.MuseumService;
import services.VisitorService;
import controllers.AbstractController;
import domain.DayPass;
import domain.Exhibition;
import domain.Museum;
import domain.Visitor;

@Controller
@RequestMapping("/dayPass/visitor")
public class DayPassVisitorController extends AbstractController {

	private final String		ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private DayPassService		dayPassService;

	@Autowired
	private MuseumService		museumService;

	@Autowired
	private VisitorService		visitorService;


	// Methods ----------------------------------------------------------------------------------------

	// v2.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false) final Integer exhibitionId, @RequestParam(required = false) final Integer museumId) {
		final ModelAndView res;

		Assert.isTrue(exhibitionId != null || museumId != null);
		Assert.isTrue(!(exhibitionId != null && museumId != null));

		DayPass dayPass = null;

		if (exhibitionId != null) {

			final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
			Assert.notNull(exhibition);

			Assert.isTrue(this.dayPassService.canBuyADayPass(exhibition));

			dayPass = this.dayPassService.create(exhibition);

		} else if (museumId != null) {

			final Museum museum = this.museumService.findOne(museumId);
			Assert.notNull(museum);

			dayPass = this.dayPassService.create(museum);
		}

		res = this.createEditModelAndView(dayPass);

		return res;
	}

	// v2.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(value = "d-1330111-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Page<DayPass> pageResult = this.dayPassService.getByPrincipal(page, 5);
		final Collection<DayPass> dayPasses = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("dayPass/listMine");

		res.addObject("dayPasses", dayPasses);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v2.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@RequestParam(required = false) final Integer exhibitionId, @RequestParam(required = false) final Integer museumId, final DayPass prunedDayPass, final BindingResult binding) {
		ModelAndView res = null;

		Assert.isTrue(exhibitionId != null || museumId != null);
		Assert.isTrue(!(exhibitionId != null && museumId != null));

		if (exhibitionId != null) {
			final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
			Assert.notNull(exhibition);

			prunedDayPass.setExhibition(exhibition);

		} else if (museumId != null) {
			final Museum museum = this.museumService.findOne(museumId);
			Assert.notNull(museum);

			prunedDayPass.setMuseum(museum);
		}

		final DayPass dayPass = this.dayPassService.reconstruct(prunedDayPass, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedDayPass);
		else
			try {
				res = new ModelAndView("redirect:/dayPass/visitor/listMine.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(dayPass, "dayPass.commit.error");
			}

		return res;

	}
	// Ancillary Methods ------------------------------------------------------------------------------

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final DayPass dayPass) {
		ModelAndView res;
		res = this.createEditModelAndView(dayPass, null);

		return res;
	}

	// v1.0 - Alicia
	private ModelAndView createEditModelAndView(final DayPass dayPass, final String message) {
		final ModelAndView res;

		res = new ModelAndView("dayPass/edit");

		res.addObject("dayPass", dayPass);
		res.addObject("message", message);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
