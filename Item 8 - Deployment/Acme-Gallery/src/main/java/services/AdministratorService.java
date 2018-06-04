
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Exhibition;
import domain.Group;
import domain.Guide;
import domain.Museum;

@Service
@Transactional
public class AdministratorService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

	public Administrator findByUserAccount(final UserAccount userAccount) {

		//Safety mesurements in supermethod

		return super.findByUserAccount(userAccount, Administrator.class);
	}

	// ---------------------------------------- QUERY #1 ----------------------------------------------

	// v1.0 - Alicia
	public Double getAvgMuseumsPerDirector() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgMuseumsPerDirector();
	}

	// v1.0 - Alicia
	public Double getMinMuseumsPerDirector() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.minMuseumsPerDirector();
	}

	// v1.0 - Alicia
	public Double getMaxMuseumsPerDirector() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.maxMuseumsPerDirector();
	}

	// v1.0 - Alicia
	public Double getStdMuseumsPerDirector() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdMuseumsPerDirector();
	}

	// ---------------------------------------- QUERY #2 ----------------------------------------------

	// v1.0 - Alicia
	public Double getAvgRatioPrivateVSPublicExhibitionsPerMuseum() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);
		Double ratio = 0.0;
		Double avg = 0.0;

		final Collection<Integer> museumsWithPublicExhibitions = this.administratorRepository.museumWithPublicExhibitions();

		for (final Integer m : museumsWithPublicExhibitions)
			ratio += (this.administratorRepository.privateExhibitionsByMuseumId(m) / this.administratorRepository.publicExhibitionsByMuseumId(m));

		avg = ratio / museumsWithPublicExhibitions.size();

		return avg;
	}

	// ---------------------------------------- QUERY #3 ----------------------------------------------

	// v1.0 - Alicia
	public Collection<Exhibition> getTop5MoreVisitedPrivateExhibitions() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Exhibition> pageResult = this.administratorRepository.top5MoreVisitedPrivateExhibitions(new PageRequest(0, 5));
		final Collection<Exhibition> exhibitions = pageResult.getContent();

		return exhibitions;
	}

	// ---------------------------------------- QUERY #4 ----------------------------------------------

	// v1.0 - Alicia
	public Double getAvgPricePrivateDayPasses() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgPricePrivateDayPasses();
	}

	// v1.0 - Alicia
	public Double getMinPricePrivateDayPasses() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.minPricePrivateDayPasses();
	}

	// v1.0 - Alicia
	public Double getMaxPricePrivateDayPasses() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.maxPricePrivateDayPasses();
	}

	// v1.0 - Alicia
	public Double getStdPricePrivateDayPasses() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdPricePrivateDayPasses();
	}

	// ---------------------------------------- QUERY #5 ----------------------------------------------

	// v1.0 - Alicia
	public Double getAvgDayPassesPerMuseum() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgDayPassesPerMuseum();
	}

	// v1.0 - Alicia
	public Double getMinDayPassesPerMuseum() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.minDayPassesPerMuseum();
	}

	// v1.0 - Alicia
	public Double getMaxDayPassesPerMuseum() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.maxDayPassesPerMuseum();
	}

	// v1.0 - Alicia
	public Double getStdDayPassesPerMuseum() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdDayPassesPerMuseum();
	}

	// ---------------------------------------- QUERY #6 ----------------------------------------------

	// v1.0 - Alicia
	public Double getRatioRejectedSponsorships() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioRejectedSponsorships();
	}

	// ---------------------------------------- QUERY #7 ----------------------------------------------

	// v1.0 - Alicia
	public Double getRatioAcceptedSponsorships() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioAcceptedSponsorships();
	}

	// ---------------------------------------- QUERY #8 ----------------------------------------------

	// v1.0 - Alicia
	public Collection<Exhibition> getExhibitions10MoreSponsorhipsThanAvg() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Collection<Exhibition> res = this.administratorRepository.exhibitions10MoreSponsorshipsThanAvg();
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getExhibitions10MoreSponsorhipsThanAvg(final int page, final int size) {
		final Page<Exhibition> res = this.administratorRepository.exhibitions10MoreSponsorshipsThanAvg(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// ---------------------------------------- QUERY #9 ----------------------------------------------

	// v1.0 - Alicia
	public Collection<Guide> getTop3GuidesLessExhibitions() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Guide> pageResult = this.administratorRepository.top3GuidesLessExhibitions(new PageRequest(0, 3));
		final Collection<Guide> guides = pageResult.getContent();

		return guides;
	}

	// ---------------------------------------- QUERY #10 ---------------------------------------------

	// v1.0 - Alicia
	public Double getAvgCritiquesPerExhibition() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgCritiquesPerExhibition();
	}

	// v1.0 - Alicia
	public Double getStdCritiquesPerExhibition() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdCritiquesPerExhibition();
	}

	// ---------------------------------------- QUERY #11 ---------------------------------------------

	// v1.0 - Alicia
	public Double getAvgArtworksPerExhibition() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgArtworksPerExhibition();
	}

	// v1.0 - Alicia
	public Double getStdArtworksPerExhibition() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdArtworksPerExhibition();
	}

	// ---------------------------------------- QUERY #12 ---------------------------------------------

	// v1.0 - Alicia
	public Double getRatioBannedVisitors() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioBannedVisitors();
	}

	// ---------------------------------------- QUERY #13 ---------------------------------------------

	// v1.0 - Alicia
	public Double getRatioBannedSponsors() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioBannedSponsors();
	}

	// ---------------------------------------- QUERY #14 ---------------------------------------------

	// v1.0 - Alicia
	public Double getAvgParticipantsPerOpenGroup() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgParticipantsPerOpenGroup();
	}

	// v1.0 - Alicia
	public Double getStdParticipantsPerOpenGroup() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdParticipantsPerOpenGroup();
	}

	// ---------------------------------------- QUERY #15 ---------------------------------------------

	// v1.0 - Alicia
	public Double getRatioVisitorsCreatedGroups() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioVisitorsCreatedGroups();
	}

	// ---------------------------------------- QUERY #16 ---------------------------------------------

	// v1.0 - Alicia
	public Page<Group> getGroups75MoreAnnouncementsThanAvg(final int page, final int size) {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Group> res = this.administratorRepository.groups75MoreAnnouncementsThanAvg(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Group> getGroups75MoreAnnouncementsThanAvg() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Collection<Group> res = this.administratorRepository.groups75MoreAnnouncementsThanAvg();
		Assert.notNull(res);

		return res;
	}

	// ---------------------------------------- QUERY #17 ---------------------------------------------

	// v1.0 - Alicia
	public Double getAvgRepliesPerComment() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgRepliesPerComment();
	}

	// v1.0 - Alicia
	public Double getStdRepliesPerComment() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdRepliesPerComment();
	}

	// ---------------------------------------- QUERY #18 ---------------------------------------------

	// v1.0 - Alicia
	public Double getRatioMuseumsWithStore() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioMuseumsWithStore();
	}

	// ---------------------------------------- QUERY #19 ---------------------------------------------

	// v1.0 - Alicia
	public Collection<Museum> getTop3MuseumsHighIncidents() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Museum> pageResult = this.administratorRepository.top3MuseumsHighIncidents(new PageRequest(0, 3));
		final Collection<Museum> museums = pageResult.getContent();

		return museums;
	}
}
