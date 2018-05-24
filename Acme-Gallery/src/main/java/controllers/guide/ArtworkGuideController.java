/*
 * ArtworkGuideController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.guide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ArtworkService;
import services.ExhibitionService;
import services.GuideService;
import controllers.AbstractController;
import domain.Artwork;
import domain.Exhibition;
import domain.Guide;

@Controller
@RequestMapping("/artwork/guide")
public class ArtworkGuideController extends AbstractController {

	private final String		ACTOR_WS	= "guide/";

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ArtworkService		artworkService;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private GuideService		guideService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int artworkId) {
		final ModelAndView res;

		final Artwork artwork = this.artworkService.findOne(artworkId);
		Assert.notNull(artwork);

		res = new ModelAndView("artwork/display");

		res.addObject("artwork", artwork);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int exhibitionId) {
		final ModelAndView result;
		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);
		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(guide);

		Assert.isTrue(exhibition.getRoom().getMuseum().getGuides().contains(guide));

		final Artwork artwork = this.artworkService.create(exhibition);

		result = this.createEditModelAndView(artwork);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Artwork prunedArtwork, final int exhibitionId, final BindingResult binding) {
		ModelAndView res = null;
		final Exhibition exhibition = this.exhibitionService.findOne(exhibitionId);
		Assert.notNull(exhibition);

		prunedArtwork.setExhibition(exhibition);

		final Artwork artwork = this.artworkService.reconstruct(prunedArtwork, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(prunedArtwork);
		else
			try {
				final Artwork saved = this.artworkService.save(artwork);
				res = new ModelAndView("redirect:/museum/guide/display.do?museumId=" + saved.getExhibition().getRoom().getMuseum().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(artwork, "artwork.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Artwork artwork) {
		return this.createEditModelAndView(artwork, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Artwork artwork, final String message) {
		ModelAndView result;
		result = new ModelAndView("artwork/edit");
		result.addObject("artwork", artwork);
		result.addObject("message", message);
		result.addObject("exhibition", artwork.getExhibition());
		return result;
	}
}
