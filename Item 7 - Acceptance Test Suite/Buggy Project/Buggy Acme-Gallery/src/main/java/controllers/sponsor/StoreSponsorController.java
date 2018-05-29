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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProductService;
import services.StoreService;
import controllers.AbstractController;
import domain.Product;
import domain.Store;

@Controller
@RequestMapping("/store/sponsor")
public class StoreSponsorController extends AbstractController {

	private final String	ACTOR_WS	= "sponsor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private StoreService	storeService;

	@Autowired
	private ProductService	productService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int storeId, @RequestParam(value = "d-1342853-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;
		final Store store = this.storeService.findOne(storeId);
		Assert.notNull(store);

		final Page<Product> pageResult = this.productService.findAllPaginatedByStore(page, 5, store);
		final Collection<Product> products = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("store/display");
		res.addObject("store", store);
		res.addObject("products", products);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		return res;
	}
}
