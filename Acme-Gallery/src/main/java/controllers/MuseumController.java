
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

import services.MuseumService;
import services.ReviewService;
import domain.Museum;
import domain.Review;

@Controller
@RequestMapping("/museum")
public class MuseumController extends AbstractController {

	//Services
	@Autowired
	private MuseumService	museumService;

	@Autowired
	private ReviewService	reviewService;


	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int museumId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		//Reviews are also listed in a Museum's Profile
		final Page<Review> pageResult = this.reviewService.findAllByMuseum(museum, page, 5);
		final Collection<Review> reviews = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("museum/display");
		res.addObject("museum", museum);
		res.addObject("reviews", reviews);
		res.addObject("resultSize", resultSize);

		return res;
	}

}
