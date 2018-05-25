
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorService;
import domain.Sponsor;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController {

	//Services
	@Autowired
	private SponsorService	sponsorService;


	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {

		final ModelAndView res;

		final ActorRegistrationForm newSponsorForm = new ActorRegistrationForm();
		res = this.createEditModelAndView(newSponsorForm);

		return res;

	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {

		ModelAndView res;
		final Sponsor sponsorToRegister;

		sponsorToRegister = this.sponsorService.reconstructRegisterForm(actorRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(actorRegistrationForm);
		else
			try {

				this.sponsorService.register(sponsorToRegister);

				res = new ModelAndView("welcome/index");

			} catch (final DataIntegrityViolationException oops) {
				res = this.createEditModelAndView(actorRegistrationForm, "actor.userAccount.username.duplicated");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(actorRegistrationForm, "actor.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final ActorRegistrationForm actorRegistrationForm) {

		final ModelAndView res;

		res = this.createEditModelAndView(actorRegistrationForm, null);

		return res;

	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final ActorRegistrationForm actorRegistrationForm, final String message) {

		final ModelAndView res;

		res = new ModelAndView("actor/register");
		res.addObject("actorRegistrationForm", actorRegistrationForm);
		res.addObject("message", message);
		res.addObject("actor", "sponsor");

		return res;
	}

}
