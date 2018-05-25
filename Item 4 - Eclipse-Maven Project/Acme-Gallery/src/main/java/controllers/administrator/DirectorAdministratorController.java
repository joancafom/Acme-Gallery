
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdministratorService;
import services.DirectorService;
import services.MuseumService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Director;
import domain.Museum;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/director/administrator")
public class DirectorAdministratorController extends AbstractController {

	private final String			ACTOR_WS	= "administrator/";

	//Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private DirectorService			directorService;

	@Autowired
	private MuseumService			museumService;


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
		final Director directorToRegister;

		directorToRegister = this.directorService.reconstructRegisterForm(actorRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(actorRegistrationForm);
		else
			try {

				this.directorService.register(directorToRegister);

				res = new ModelAndView("welcome/index");

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
		res.addObject("actor", "director");
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value = "d-3996330-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Director> pageResult = this.directorService.findAllPaginated(page, 5);
		final Collection<Director> directors = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("director/list");
		res.addObject("directors", directors);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int directorId, @RequestParam(value = "d-447220-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;
		final Director director = this.directorService.findOne(directorId);
		Assert.notNull(director);

		final Page<Museum> pageResult = this.museumService.findAllPaginatedByDirector(page, 5, director);
		final Collection<Museum> museums = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("director/display");
		res.addObject("director", director);
		res.addObject("museums", museums);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}

}
