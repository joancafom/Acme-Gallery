/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.DirectorService;
import services.ProductService;
import services.StoreService;
import controllers.AbstractController;
import domain.Director;
import domain.Product;
import domain.Store;

@Controller
@RequestMapping("/product/director")
public class ProductDirectorController extends AbstractController {

	private final String	ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ProductService	productService;

	@Autowired
	private StoreService	storeService;

	@Autowired
	private DirectorService	directorService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int productId, @RequestParam(required = false) final Boolean deleteConfirmation) {
		final ModelAndView res;
		final Product product = this.productService.findOne(productId);
		Assert.notNull(product);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		res = new ModelAndView("product/display");
		res.addObject("product", product);
		res.addObject("actorWS", this.ACTOR_WS);

		if (product.getStore().getMuseum().getDirector().equals(director))
			res.addObject("own", true);

		if (deleteConfirmation == null)
			res.addObject("showConfirmation", false);
		else if (deleteConfirmation == true)
			res.addObject("showConfirmation", true);

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int storeId) {
		final ModelAndView result;
		final Store store = this.storeService.findOne(storeId);
		Assert.notNull(store);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		Assert.isTrue(store.getMuseum().getDirector().equals(director));

		final Product product = this.productService.create(store);

		result = this.createEditModelAndView(product);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int productId) {
		ModelAndView result;
		final Product product = this.productService.findOne(productId);
		Assert.notNull(product);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		Assert.isTrue(product.getStore().getMuseum().getDirector().equals(director));
		try {
			this.productService.delete(product);
			result = new ModelAndView("redirect:/store/director/display.do?storeId=" + product.getStore().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:display.do?productId=" + productId);
			result.addObject("message", "product.commit.error");
		}

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Product prunedProduct, final BindingResult binding) {
		ModelAndView res = null;

		final Product product = this.productService.reconstruct(prunedProduct, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedProduct);
		else
			try {
				final Product saved = this.productService.save(product);
				res = new ModelAndView("redirect:display.do?productId=" + saved.getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(product, "museum.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Product product) {
		return this.createEditModelAndView(product, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Product product, final String message) {
		ModelAndView result;
		result = new ModelAndView("product/edit");
		result.addObject("product", product);
		result.addObject("store", product.getStore());
		result.addObject("message", message);

		return result;
	}
}
