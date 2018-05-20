
package controllers.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.VisitorService;
import controllers.AbstractController;
import domain.Visitor;
import forms.ActorEditionForm;

@Controller
@RequestMapping("/visitor/visitor")
public class VisitorVisitorController extends AbstractController {

	//Services
	@Autowired
	private VisitorService	visitorService;


	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView res;

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		res = new ModelAndView("visitor/display");
		res.addObject("visitor", visitor);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView res;

		final ActorEditionForm visitorForm = new ActorEditionForm();

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		visitorForm.setName(visitor.getName());
		visitorForm.setSurnames(visitor.getSurnames());
		visitorForm.setEmail(visitor.getEmail());
		visitorForm.setPhoneNumber(visitor.getPhoneNumber());
		visitorForm.setAddress(visitor.getAddress());
		visitorForm.setGender(visitor.getGender());

		res = this.createEditModelAndView(visitorForm);

		return res;

	}

	// v1.0 - Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final ActorEditionForm actorEditionForm, final BindingResult binding) {
		ModelAndView res;
		final Visitor visitorToEdit;

		visitorToEdit = this.visitorService.reconstructEditForm(actorEditionForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(actorEditionForm);
		else
			try {
				this.visitorService.saveEdit(visitorToEdit);
				res = new ModelAndView("redirect:/visitor/visitor/display.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(actorEditionForm, "visitor.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final ActorEditionForm actorEditionForm) {
		final ModelAndView res;

		res = this.createEditModelAndView(actorEditionForm, null);

		return res;

	}

	// v1.0 - Alicia
	protected ModelAndView createEditModelAndView(final ActorEditionForm actorEditionForm, final String message) {
		final ModelAndView res;

		res = new ModelAndView("visitor/edit");
		res.addObject("actorEditionForm", actorEditionForm);
		res.addObject("message", message);

		return res;
	}

}
