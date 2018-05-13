/*
 * CategorySponsorController.java
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
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ExhibitionService;
import controllers.AbstractController;
import domain.Category;
import domain.Exhibition;

@Controller
@RequestMapping("/category/sponsor")
public class CategorySponsorController extends AbstractController {

	private final String		ACTOR_WS	= "sponsor/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private ExhibitionService	exhibitionService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer categoryId) {
		final ModelAndView res;
		final Category category;

		if (categoryId != null)
			category = this.categoryService.findOne(categoryId);
		else
			category = this.categoryService.getRootCategory();

		Assert.notNull(category);

		final Collection<Category> childrenCategories = this.categoryService.getChildren(category);

		final Collection<Exhibition> exhibitions = this.exhibitionService.getByCategory(category);

		res = new ModelAndView("category/display");

		res.addObject("category", category);
		res.addObject("childrenCategories", childrenCategories);
		res.addObject("exhibitions", exhibitions);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
