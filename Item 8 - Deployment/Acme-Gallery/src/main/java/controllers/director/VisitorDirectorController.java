
package controllers.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.VisitorService;
import controllers.AbstractController;
import domain.Visitor;

@Controller
@RequestMapping("/visitor/director")
public class VisitorDirectorController extends AbstractController {

	private final String	ACTOR_WS	= "director/";

	//Services

	@Autowired
	private VisitorService	visitorService;


	/* v2.0 - josembell */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int visitorId) {
		final ModelAndView res;
		Visitor visitor = null;

		res = new ModelAndView("visitor/display");

		visitor = this.visitorService.findOne(visitorId);
		Assert.notNull(visitor);

		res.addObject("visitor", visitor);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
