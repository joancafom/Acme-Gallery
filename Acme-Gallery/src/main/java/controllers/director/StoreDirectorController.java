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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.DirectorService;
import services.MuseumService;
import services.ProductService;
import services.StoreService;
import controllers.AbstractController;
import domain.Director;
import domain.Museum;
import domain.Product;
import domain.Store;

@Controller
@RequestMapping("/store/director")
public class StoreDirectorController extends AbstractController {

	private final String	ACTOR_WS	= "director/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private StoreService	storeService;

	@Autowired
	private ProductService	productService;

	@Autowired
	private MuseumService	museumService;

	@Autowired
	private DirectorService	directorService;


	// Methods ----------------------------------------------------------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
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

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int museumId) {
		final ModelAndView res;
		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		Assert.isTrue(museum.getStore() == null);
		Assert.isTrue(director.getMuseums().contains(museum));
		Assert.isTrue(museum.getDirector().equals(director));

		final Store store = this.storeService.create(museum);

		res = this.createEditModelAndView(store);

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Store prunedStore, final int museumId, final BindingResult binding) {
		ModelAndView res = null;
		final Museum museum = this.museumService.findOne(museumId);
		Assert.notNull(museum);

		prunedStore.setMuseum(museum);

		final Store store = this.storeService.reconstruct(prunedStore, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedStore);
		else
			try {
				final Store saved = this.storeService.save(store);
				res = new ModelAndView("redirect:display.do?storeId=" + saved.getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(store, "museum.commit.error");
			}

		return res;
	}

	protected ModelAndView createEditModelAndView(final Store store) {
		return this.createEditModelAndView(store, null);
	}

	private ModelAndView createEditModelAndView(final Store store, final String message) {
		ModelAndView result;

		result = new ModelAndView("store/edit");
		result.addObject("store", store);
		result.addObject("message", message);
		result.addObject("museum", store.getMuseum());

		return result;
	}
}
