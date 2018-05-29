/*
 * CritiqueCriticController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.critic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.CriticService;
import services.CritiqueService;
import services.ExhibitionService;
import controllers.AbstractController;
import domain.Critic;
import domain.Critique;
import domain.Exhibition;

@Controller
@RequestMapping("/critique/critic")
public class CritiqueCriticController extends AbstractController {

	private final String		ACTOR_WS	= "critic/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private CritiqueService		critiqueService;

	@Autowired
	private CriticService		criticService;

	@Autowired
	private ExhibitionService	exhibitionService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - JA
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int exhibitionId, final RedirectAttributes redirectAttributes) {

		ModelAndView res;

		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		final Critique critique = this.critiqueService.create(exhibition);

		final Critic currentCritic = this.criticService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentCritic);

		//Check if she or he has already one critic or not
		final Critique oldCritique = this.critiqueService.findByCriticAndExhibition(currentCritic, exhibition);

		if (oldCritique == null)
			res = this.createEditModelAndView(critique);
		else {
			res = new ModelAndView("redirect:/exhibition/critic/display.do?exhibitionId=" + critique.getExhibition().getId());
			redirectAttributes.addFlashAttribute("message", "critic.create.already");
		}

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int critiqueId) {
		final ModelAndView res;

		final Critique critique = this.critiqueService.findOne(critiqueId);
		Assert.notNull(critique);

		res = new ModelAndView("critique/display");

		res.addObject("critique", critique);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Critique prunedCritique, final BindingResult binding) {

		ModelAndView res = null;

		final Critique critique = this.critiqueService.reconstructCreate(prunedCritique, binding);

		//Reconstruct checks for the Exhibiton being null
		prunedCritique.setExhibition(critique.getExhibition());

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedCritique);
		else
			try {
				this.critiqueService.saveCreate(critique);
				res = new ModelAndView("redirect:/exhibition/critic/display.do?exhibitionId=" + critique.getExhibition().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedCritique, "critique.commit.error");
			}

		return res;
	}
	//Ancillary Methods

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Critique critique) {

		return this.createEditModelAndView(critique, null);
	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Critique critique, final String message) {

		final ModelAndView res;

		Assert.notNull(critique);

		res = new ModelAndView("critique/edit");
		res.addObject("critique", critique);
		res.addObject("message", message);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
