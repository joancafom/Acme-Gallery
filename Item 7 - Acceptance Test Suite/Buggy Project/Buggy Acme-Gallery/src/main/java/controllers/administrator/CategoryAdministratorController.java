/*
 * CategoryAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.CategoryService;
import services.ExhibitionService;
import controllers.AbstractController;
import domain.Category;
import domain.Exhibition;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {

	private final String		ACTOR_WS	= "administrator/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private ExhibitionService	exhibitionService;


	// Methods ----------------------------------------------------------------------------------------

	// v2.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer categoryId, @RequestParam(value = "d-5218124-p", defaultValue = "1") final Integer categoriesPage,
		@RequestParam(value = "d-2511045-p", defaultValue = "1") final Integer exhibitionsPage) {
		final ModelAndView res;
		final Category category;

		if (categoryId != null)
			category = this.categoryService.findOne(categoryId);
		else
			category = this.categoryService.getRootCategory();

		Assert.notNull(category);

		final Page<Category> categoriesResult = this.categoryService.getChildren(category, categoriesPage, 5);
		final Collection<Category> childrenCategories = categoriesResult.getContent();
		final Integer categoriesSize = new Long(categoriesResult.getTotalElements()).intValue();

		final Page<Exhibition> exhibitionsResult = this.exhibitionService.getByCategory(category, exhibitionsPage, 5);
		final Collection<Exhibition> exhibitions = exhibitionsResult.getContent();
		final Integer exhibitionsSize = new Long(exhibitionsResult.getTotalElements()).intValue();

		res = new ModelAndView("category/display");

		res.addObject("category", category);
		res.addObject("childrenCategories", childrenCategories);
		res.addObject("categoriesSize", categoriesSize);
		res.addObject("exhibitions", exhibitions);
		res.addObject("exhibitionsSize", exhibitionsSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int parentCategoryId) {
		final ModelAndView res;

		final Category parentCategory = this.categoryService.findOne(parentCategoryId);
		Assert.notNull(parentCategory);

		final Category category = this.categoryService.create(parentCategory);
		res = this.createEditModelAndView(category);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int categoryId, final RedirectAttributes redirectAttributes) {

		final ModelAndView res;

		final Category toDelete = this.categoryService.findOne(categoryId);
		Assert.notNull(toDelete);
		Assert.notNull(toDelete.getParentCategory());

		res = new ModelAndView("redirect:display.do?categoryId=" + toDelete.getParentCategory().getId());

		try {
			this.categoryService.delete(toDelete);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "category.commit.error");
		}

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Category prunedCategory, final BindingResult binding) {
		ModelAndView res = null;

		final Category category = this.categoryService.reconstructEdit(prunedCategory, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedCategory);
		else
			try {
				final Category savedCategory = this.categoryService.saveCreate(category);
				res = new ModelAndView("redirect:display.do?categoryId=" + savedCategory.getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(prunedCategory, "category.commit.error");
			}

		return res;
	}

	//Ancillary Methods
	protected ModelAndView createEditModelAndView(final Category category) {
		return this.createEditModelAndView(category, null);
	}

	protected ModelAndView createEditModelAndView(final Category category, final String message) {
		final ModelAndView res;

		res = new ModelAndView("category/edit");
		res.addObject("category", category);
		res.addObject("message", message);

		return res;
	}
}
