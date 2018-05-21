
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.AdministratorService;
import services.VisitorService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Visitor;

@Controller
@RequestMapping("/visitor/administrator")
public class VisitorAdministratorController extends AbstractController {

	private final String			ACTOR_WS	= "administrator/";

	//Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private VisitorService			visitorService;


	/* v1.0 - josembell */
	@RequestMapping(value = "/listUnlocked", method = RequestMethod.GET)
	public ModelAndView listUnlocked(@RequestParam(value = "d-1345524-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Visitor> pageResult = this.visitorService.findAllUnlocked(page, 5);
		final Collection<Visitor> visitors = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("visitor/list");
		result.addObject("visitors", visitors);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("action", "listUnlocked");

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/listLocked", method = RequestMethod.GET)
	public ModelAndView listLocked(@RequestParam(value = "d-1345524-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Visitor> pageResult = this.visitorService.findAllLocked(page, 5);
		final Collection<Visitor> visitors = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("visitor/list");
		result.addObject("visitors", visitors);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("action", "listLocked");

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int visitorId, final RedirectAttributes redirectAttributes) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Visitor visitor = this.visitorService.findOne(visitorId);
		Assert.notNull(visitor);
		Assert.isTrue(visitor.getUserAccount().getIsLocked() == false);

		result = new ModelAndView("redirect:listLocked.do");

		try {
			this.visitorService.ban(visitor);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "visitor.commit.error");
		}

		return result;
	}

}
