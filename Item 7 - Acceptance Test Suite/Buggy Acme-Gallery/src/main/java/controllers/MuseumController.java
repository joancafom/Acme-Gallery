
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ExhibitionService;
import services.MuseumService;
import services.ReviewService;
import domain.Exhibition;
import domain.Museum;
import domain.Review;

@Controller
@RequestMapping("/museum")
public class MuseumController extends AbstractController {

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private MuseumService		museumService;

	@Autowired
	private ReviewService		reviewService;

	@Autowired
	private ExhibitionService	exhibitionService;


	// Methods ----------------------------------------------------------------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int museumId, @RequestParam(value = "d-447218-p", defaultValue = "1") final Integer pageR, @RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer pageE) {

		final ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		//Reviews are also listed in a Museum's Profile
		final Page<Review> pageResultR = this.reviewService.findAllByMuseum(museum, pageR, 5);
		final Collection<Review> reviews = pageResultR.getContent();
		final Integer resultSizeR = new Long(pageResultR.getTotalElements()).intValue();

		//Exhibition are also listed in a Museum's Profile (in this case, current and future ones)
		final Page<Exhibition> pageResultE = this.exhibitionService.getCurrentAndFutureByMuseum(museum, pageE, 5);
		final Collection<Exhibition> exhibitions = pageResultE.getContent();
		final Integer resultSizeE = new Long(pageResultE.getTotalElements()).intValue();

		//Average Rating
		final Double avgRating = this.museumService.getAvgRating(museum);

		res = new ModelAndView("museum/display");
		res.addObject("museum", museum);
		res.addObject("reviews", reviews);
		res.addObject("resultSizeR", resultSizeR);
		res.addObject("exhibitions", exhibitions);
		res.addObject("resultSizeE", resultSizeE);
		res.addObject("avgRating", avgRating);

		return res;
	}

	// v1.0 - Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listResults(@RequestParam(value = "d-447220-p", defaultValue = "1") final Integer page) {
		ModelAndView res;

		final Page<Museum> pageResult = this.museumService.findAllPaginated(page, 5);
		final Collection<Museum> museums = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("museum/list");
		res.addObject("landing", "list");
		res.addObject("museums", museums);
		res.addObject("resultSize", resultSize);

		return res;
	}

}
