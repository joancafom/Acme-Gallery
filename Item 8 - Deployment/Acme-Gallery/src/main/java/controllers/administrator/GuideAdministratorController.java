
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdministratorService;
import services.GuideService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Guide;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/guide/administrator")
public class GuideAdministratorController extends AbstractController {

	private final String			ACTOR_WS	= "administrator/";

	//Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private GuideService			guideService;


	// v1.0 - Alicia
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		final ModelAndView res;

		final ActorRegistrationForm newVisitorForm = new ActorRegistrationForm();
		res = this.createEditModelAndView(newVisitorForm);

		return res;

	}

	// v1.0 - Alicia
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		ModelAndView res;
		final Guide guideToRegister;

		guideToRegister = this.guideService.reconstructRegisterForm(actorRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(actorRegistrationForm);
		else
			try {

				this.guideService.register(guideToRegister);

				res = new ModelAndView("redirect:/");

			} catch (final DataIntegrityViolationException oops) {
				res = this.createEditModelAndView(actorRegistrationForm, "actor.userAccount.username.duplicated");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(actorRegistrationForm, "actor.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final ActorRegistrationForm actorRegistrationForm) {

		final ModelAndView res;

		res = this.createEditModelAndView(actorRegistrationForm, null);

		return res;

	}

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final ActorRegistrationForm actorRegistrationForm, final String message) {

		final ModelAndView res;

		res = new ModelAndView("actor/register");
		res.addObject("actorRegistrationForm", actorRegistrationForm);
		res.addObject("message", message);
		res.addObject("actor", "guide");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
