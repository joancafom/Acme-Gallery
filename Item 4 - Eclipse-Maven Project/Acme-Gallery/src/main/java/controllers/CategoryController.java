/*
 * CategoryController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

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

import services.CategoryService;
import services.ExhibitionService;
import domain.Category;
import domain.Exhibition;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

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

		return res;
	}
}
