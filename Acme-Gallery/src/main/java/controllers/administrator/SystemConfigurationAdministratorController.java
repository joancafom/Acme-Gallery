/*
 * SystemConfigurationAdministratorController.java
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.SystemConfigurationService;
import controllers.AbstractController;

@Controller
@RequestMapping("/systemConfiguration/administrator")
public class SystemConfigurationAdministratorController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// v1.0 - JA
	@RequestMapping(value = "/listTabooWords", method = RequestMethod.GET)
	public ModelAndView listTabooWords() {

		final ModelAndView res;

		final Collection<String> tabooWords = this.systemConfigurationService.getTabooWords();

		res = new ModelAndView("systemConfiguration/list");
		res.addObject("tabooWords", tabooWords);

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/addTabooWord", method = RequestMethod.GET)
	public ModelAndView addTabooWord() {
		final ModelAndView result;

		result = this.createEditModelAndView("");

		return result;
	}

	// v1.0 - JA
	@RequestMapping(value = "/deleteTabooWord", method = RequestMethod.GET)
	public ModelAndView deleteTabooWord(@RequestParam final String tabooWord, final RedirectAttributes redirectAttributes) {

		final ModelAndView res = new ModelAndView("redirect:listTabooWords.do");

		try {
			this.systemConfigurationService.deleteTabooWord(tabooWord);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "systemConfiguration.commit.error");
		}

		return res;
	}

	// v1.0 - JA
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editTabooWord(@RequestParam final String tabooWord, final RedirectAttributes redirectAttributes) {

		ModelAndView res;

		try {
			this.systemConfigurationService.addTabooWord(tabooWord);
			res = new ModelAndView("redirect:listTabooWords.do");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(tabooWord, "systemConfiguration.commit.error");
			redirectAttributes.addFlashAttribute("message", "systemConfiguration.commit.error");
		}

		return res;
	}

	//Ancillary Methods

	protected ModelAndView createEditModelAndView(final String tabooWord) {

		return this.createEditModelAndView(tabooWord, null);
	}

	private ModelAndView createEditModelAndView(final String tabooWord, final String message) {

		final ModelAndView res = new ModelAndView("systemConfiguration/edit");

		res.addObject("tabooWord", tabooWord);
		res.addObject("message", message);

		return res;
	}
}
