
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
import services.SponsorService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Sponsor;

@Controller
@RequestMapping("/sponsor/administrator")
public class SponsorAdministratorController extends AbstractController {

	private final String			ACTOR_WS	= "administrator/";

	//Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private SponsorService			sponsorService;


	/* v1.0 - josembell */
	@RequestMapping(value = "/listUnlocked", method = RequestMethod.GET)
	public ModelAndView listUnlocked(@RequestParam(value = "d-1344840-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Sponsor> pageResult = this.sponsorService.findAllUnlocked(page, 5);
		final Collection<Sponsor> sponsors = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("sponsor/list");
		result.addObject("sponsors", sponsors);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("action", "listUnlocked");

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/listLocked", method = RequestMethod.GET)
	public ModelAndView listLocked(@RequestParam(value = "d-1344840-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Sponsor> pageResult = this.sponsorService.findAllLocked(page, 5);
		final Collection<Sponsor> sponsors = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("sponsor/list");
		result.addObject("sponsors", sponsors);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("action", "listLocked");

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int sponsorId, final RedirectAttributes redirectAttributes) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Sponsor sponsor = this.sponsorService.findOne(sponsorId);
		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getUserAccount().getIsLocked() == false);

		result = new ModelAndView("redirect:listLocked.do");

		try {
			this.sponsorService.ban(sponsor);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "sponsor.commit.error");
		}

		return result;
	}

}
