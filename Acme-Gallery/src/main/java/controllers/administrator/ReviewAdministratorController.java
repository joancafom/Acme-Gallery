
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
import services.ReviewService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Review;

@Controller
@RequestMapping("/review/administrator")
public class ReviewAdministratorController extends AbstractController {

	private final String			ACTOR_WS	= "administrator/";

	//Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ReviewService			reviewService;


	/* v1.0 - josembell */
	@RequestMapping(value = "/listTaboo", method = RequestMethod.GET)
	public ModelAndView listTabooed(@RequestParam(value = "d-447218-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Review> pageResult = this.reviewService.findTabooed(page, 5);
		final Collection<Review> reviews = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("review/list");
		result.addObject("reviews", reviews);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int reviewId, final RedirectAttributes redirectAttributes) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Review review = this.reviewService.findOne(reviewId);
		Assert.notNull(review);

		result = new ModelAndView("redirect:listTaboo.do");

		try {
			this.reviewService.delete(review);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "review.commit.error");
		}

		return result;
	}

}
