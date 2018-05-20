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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DayPassService;
import services.ExhibitionService;
import controllers.AbstractController;
import domain.DayPass;
import domain.Exhibition;

@Controller
@RequestMapping("/dayPass/visitor")
public class DayPassVisitorController extends AbstractController {

	private final String		ACTOR_WS	= "visitor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private DayPassService		dayPassService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int exhibitionId) {
		final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		Assert.isTrue(this.dayPassService.canBuyADayPass(exhibition));

		final DayPass dayPass = this.dayPassService.create(exhibition);

		res = this.createEditModelAndView(dayPass);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int exhibitionId) {
		//final ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		//final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(exhibition);
		//Assert.notNull(director);

		//Assert.isTrue(director.getMuseums().contains(exhibition.getRoom().getMuseum()));
		Assert.isTrue(exhibition.getStartingDate().after(new Date()));

		//res = this.createEditModelAndView(exhibition);

		//return res;
		return null;

	}

	// v1.0 - Alicia
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		//final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		//Assert.notNull(director);

		//final Page<Exhibition> pageResult = this.exhibitionService.getByDirector(director, page, 5);
		//final Collection<Exhibition> exhibitions = pageResult.getContent();
		//final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("exhibition/listMine");

		//res.addObject("exhibitions", exhibitions);
		//res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final DayPass prunedDayPass, final BindingResult binding) {
		@SuppressWarnings("unused")
		final ModelAndView res;

		final DayPass dayPass = this.dayPassService.reconstruct(prunedDayPass, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedDayPass);
		else
			try {
				@SuppressWarnings("unused")
				final DayPass dayPassS = this.dayPassService.saveCreateAndEdit(dayPass);
				//res = new ModelAndView("redirect:/exhibition/director/display.do?exhibitionId=" + exhibitionS.getId());
			} catch (final Throwable oops) {
				//res = this.createEditModelAndView(exhibition, "exhibition.commit.error");
			}

		return null;

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
