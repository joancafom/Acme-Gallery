/*
 * AdministratorController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import domain.Exhibition;
import domain.Group;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// Methods ----------------------------------------------------------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping("/dashboard")
	public ModelAndView dashboard(@RequestParam(value = "d-7533201-p", defaultValue = "1") final Integer exhibitionsPage, @RequestParam(value = "d-148477-p", defaultValue = "1") final Integer groupsPage) {
		final ModelAndView res;
		res = new ModelAndView("administrator/dashboard");

		res.addObject("avgMuseumsPerDirector", this.administratorService.getAvgMuseumsPerDirector());
		res.addObject("minMuseumsPerDirector", this.administratorService.getMinMuseumsPerDirector());
		res.addObject("maxMuseumsPerDirector", this.administratorService.getMaxMuseumsPerDirector());
		res.addObject("stdMuseumsPerDirector", this.administratorService.getStdMuseumsPerDirector());

		res.addObject("avgRatioPrivateVSPublicExhibitionsPerMuseum", this.administratorService.getAvgRatioPrivateVSPublicExhibitionsPerMuseum());

		res.addObject("top5MoreVisitedPrivateExhibitions", this.administratorService.getTop5MoreVisitedPrivateExhibitions());

		res.addObject("avgPricePrivateDayPasses", this.administratorService.getAvgPricePrivateDayPasses());
		res.addObject("minPricePrivateDayPasses", this.administratorService.getMinPricePrivateDayPasses());
		res.addObject("maxPricePrivateDayPasses", this.administratorService.getMaxPricePrivateDayPasses());
		res.addObject("stdPricePrivateDayPasses", this.administratorService.getStdPricePrivateDayPasses());

		res.addObject("avgDayPassesPerMuseum", this.administratorService.getAvgDayPassesPerMuseum());
		res.addObject("minDayPassesPerMuseum", this.administratorService.getMinDayPassesPerMuseum());
		res.addObject("maxDayPassesPerMuseum", this.administratorService.getMaxDayPassesPerMuseum());
		res.addObject("stdDayPassesPerMuseum", this.administratorService.getStdDayPassesPerMuseum());

		res.addObject("ratioRejectedSponsorships", this.administratorService.getRatioRejectedSponsorships());

		res.addObject("ratioAcceptedSponsorships", this.administratorService.getRatioAcceptedSponsorships());

		final Page<Exhibition> exhibitionsResult = this.administratorService.getExhibitions10MoreSponsorhipsThanAvg(exhibitionsPage, 5);
		final Collection<Exhibition> exhibitions10MoreSponsorhipsThanAvg = exhibitionsResult.getContent();
		final Integer exhibitionsSize = new Long(exhibitionsResult.getTotalElements()).intValue();
		res.addObject("exhibitions10MoreSponsorhipsThanAvg", exhibitions10MoreSponsorhipsThanAvg);
		res.addObject("exhibitionsSize", exhibitionsSize);

		res.addObject("top3GuidesLessExhibitions", this.administratorService.getTop3GuidesLessExhibitions());

		res.addObject("avgCritiquesPerExhibition", this.administratorService.getAvgCritiquesPerExhibition());
		res.addObject("stdCritiquesPerExhibition", this.administratorService.getStdCritiquesPerExhibition());

		res.addObject("avgHighlightsPerExhibition", this.administratorService.getAvgHighlightsPerExhibition());
		res.addObject("stdHighlightsPerExhibition", this.administratorService.getStdHighlightsPerExhibition());

		res.addObject("ratioBannedVisitors", this.administratorService.getRatioBannedVisitors());

		res.addObject("ratioBannedSponsors", this.administratorService.getRatioBannedSponsors());

		res.addObject("avgParticipantsPerOpenGroup", this.administratorService.getAvgParticipantsPerOpenGroup());
		res.addObject("stdParticipantsPerOpenGroup", this.administratorService.getStdParticipantsPerOpenGroup());

		res.addObject("ratioVisitorsCreatedGroups", this.administratorService.getRatioVisitorsCreatedGroups());

		final Page<Group> groupsResult = this.administratorService.getGroups75MoreAnnouncementsThanAvg(groupsPage, 5);
		final Collection<Group> groups75MoreAnnouncementsThanAvg = groupsResult.getContent();
		final Integer groupsSize = new Long(groupsResult.getTotalElements()).intValue();
		res.addObject("groups75MoreAnnouncementsThanAvg", groups75MoreAnnouncementsThanAvg);
		res.addObject("groupsSize", groupsSize);

		res.addObject("avgRepliesPerComment", this.administratorService.getAvgRepliesPerComment());
		res.addObject("stdRepliesPerComment", this.administratorService.getStdRepliesPerComment());

		res.addObject("ratioMuseumsWithStore", this.administratorService.getRatioMuseumsWithStore());

		res.addObject("top3MuseumsHighIncidents", this.administratorService.getTop3MuseumsHighIncidents());

		res.addObject("actorWS", "administrator/");

		return res;
	}
}
