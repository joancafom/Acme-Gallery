/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProductService;
import controllers.AbstractController;
import domain.Product;

@Controller
@RequestMapping("/product/sponsor")
public class ProductSponsorController extends AbstractController {

	private final String	ACTOR_WS	= "sponsor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ProductService	productService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int productId) {
		final ModelAndView res;
		final Product product = this.productService.findOne(productId);
		Assert.notNull(product);

		res = new ModelAndView("product/display");
		res.addObject("product", product);
		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}
}
