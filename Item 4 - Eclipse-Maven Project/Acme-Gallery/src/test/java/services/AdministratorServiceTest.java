
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.DayPass;
import domain.Director;
import domain.Exhibition;
import domain.Group;
import domain.Guide;
import domain.Museum;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Visitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private AdministratorService	administratorService;

	//Helping Services

	@Autowired
	private DayPassService			dayPassService;

	@Autowired
	private DirectorService			directorService;

	@Autowired
	private MuseumService			museumService;

	@Autowired
	private ExhibitionService		exhibitionService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private GuideService			guideService;

	@Autowired
	private VisitorService			visitorService;

	@Autowired
	private GroupService			groupService;

	@Autowired
	private SponsorService			sponsorService;

	@PersistenceContext
	private EntityManager			entityManager;


	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//              * The average, the minimum, the maximum and the standard deviation for the number of museums managed per director.
	//              * The average ratio of private vs public exhibitions per museum, having at least one public exhibition.
	//              * The top 5 more visited private exhibitions.
	//				* The average, the minimum, the maximum and the standard deviation of the
	//				  price of the private day passes.
	//				* The average, the minimum, the maximum and the standard deviation for the
	//				  number of day passes sold per museum.
	//				* The ratio of sponsorships with status 'REJECTED'.
	//				* The ratio of sponsorships with status 'ACCEPTED'.
	//				* The listing of exhibitions that have at least 10% more sponsorships than the
	//				  average.
	//				* The top 3 guides with less exhibitions.
	//				* The average and the standard deviation of the number of critiques per exhibition.
	//				* The average and the standard deviation of the number of artworks per exhibition.
	//				* The ratio of banned visitors.
	//				* The ratio of banned sponsors.
	//				* The average and the standard deviation of the number of participants per
	//				  open group.
	//				* The ratio of visitors who have ever created a group.
	//				* The groups whose number of announcements is above 75% the average number of announcements per group.
	//				* The average and the standard deviation of replies per comment.
	//				* The ratio of museums with a gift store.
	//				* The 3 museums with more high-severity incidents.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverAvgMinMaxStdMuseumsPerDirector() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgMinMaxStdMuseumsPerDirector((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgMinMaxStdMuseumsPerDirector(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgMuseumsPerDirector();
			final Double minRetrieved = this.administratorService.getMinMuseumsPerDirector();
			final Double maxRetrieved = this.administratorService.getMaxMuseumsPerDirector();
			final Double stdRetrieved = this.administratorService.getStdMuseumsPerDirector();

			//Compute the values in order to check them
			final List<Integer> museumsPerDirector = new ArrayList<Integer>();

			for (final Director d : this.directorService.findAll())
				museumsPerDirector.add(d.getMuseums().size());

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double minRetrievedRounded = this.roundNumber(minRetrieved, 3);
			final Double maxRetrievedRounded = this.roundNumber(maxRetrieved, 3);
			final Double stdRetrievedRounded = this.roundNumber(stdRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(museumsPerDirector), 3);
			final Double minComputedRounded = this.roundNumber(this.computeMin(museumsPerDirector), 3);
			final Double maxComputedRounded = this.roundNumber(this.computeMax(museumsPerDirector), 3);
			final Double stdComputedRounded = this.roundNumber(this.computeStd(museumsPerDirector), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));
			Assert.isTrue(minRetrievedRounded.equals(minComputedRounded));
			Assert.isTrue(maxRetrievedRounded.equals(maxComputedRounded));
			Assert.isTrue(stdRetrievedRounded.equals(stdComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//              * The average ratio of private vs public exhibitions per museum, having at least one public exhibition.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------
	@Test
	public void driverAvgRatioPrivateVSPublicExhibitionsPerMuseum() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgRatioPrivateVSPublicExhibitionsPerMuseum((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgRatioPrivateVSPublicExhibitionsPerMuseum(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgRatioPrivateVSPublicExhibitionsPerMuseum();

			//Compute the values in order to check them
			final List<Double> ratioComputed = new ArrayList<Double>();

			final List<Exhibition> privateExhibitions = new ArrayList<Exhibition>();
			final List<Exhibition> publicExhibitions = new ArrayList<Exhibition>();

			for (final Museum m : this.museumService.findAll()) {
				privateExhibitions.clear();
				publicExhibitions.clear();

				for (final Exhibition e : this.exhibitionService.getAllByMuseum(m))
					if (e.getIsPrivate())
						privateExhibitions.add(e);
					else
						publicExhibitions.add(e);

				if (publicExhibitions.size() > 0)
					ratioComputed.add(privateExhibitions.size() * 1.0 / publicExhibitions.size());

			}

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(ratioComputed), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//              * The top 5 more visited private exhibitions.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverTop5MoreVisitedPrivateExhibitions() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateTop5MoreVisitedPrivateExhibitions((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateTop5MoreVisitedPrivateExhibitions(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final List<Exhibition> top5Retrieved = new ArrayList<Exhibition>(this.administratorService.getTop5MoreVisitedPrivateExhibitions());
			List<Exhibition> top5Computed = new ArrayList<Exhibition>();

			final Comparator<Exhibition> moreDayPasses = new Comparator<Exhibition>() {

				@Override
				public int compare(final Exhibition o1, final Exhibition o2) {

					if (o1.getDayPasses().size() < o2.getDayPasses().size())
						return 1;
					else if (o1.getDayPasses().size() == o2.getDayPasses().size())
						return 0;
					else
						return -1;

				}
			};

			for (final Exhibition e : this.exhibitionService.findAll())
				if (e.getIsPrivate())
					top5Computed.add(e);

			Collections.sort(top5Computed, moreDayPasses);
			top5Computed = top5Computed.subList(0, 5 > top5Computed.size() ? top5Computed.size() - 1 : 5);

			Assert.isTrue(top5Retrieved.equals(top5Computed));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The average, the minimum, the maximum and the standard deviation of the
	//				  price of the private day passes.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverAvgMinMaxStdPricePrivateDayPasses() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgMinMaxStdPricePrivateDayPasses((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgMinMaxStdPricePrivateDayPasses(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgPricePrivateDayPasses();
			final Double minRetrieved = this.administratorService.getMinPricePrivateDayPasses();
			final Double maxRetrieved = this.administratorService.getMaxPricePrivateDayPasses();
			final Double stdRetrieved = this.administratorService.getStdPricePrivateDayPasses();

			//Compute the values in order to check them
			final List<Double> dayPassesPrices = new ArrayList<Double>();

			for (final DayPass d : this.dayPassService.findAll())
				if (d.getExhibition() != null && d.getExhibition().getIsPrivate() == true)
					dayPassesPrices.add(d.getPrice());

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double minRetrievedRounded = this.roundNumber(minRetrieved, 3);
			final Double maxRetrievedRounded = this.roundNumber(maxRetrieved, 3);
			final Double stdRetrievedRounded = this.roundNumber(stdRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(dayPassesPrices), 3);
			final Double minComputedRounded = this.roundNumber(this.computeMin(dayPassesPrices), 3);
			final Double maxComputedRounded = this.roundNumber(this.computeMax(dayPassesPrices), 3);
			final Double stdComputedRounded = this.roundNumber(this.computeStd(dayPassesPrices), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));
			Assert.isTrue(minRetrievedRounded.equals(minComputedRounded));
			Assert.isTrue(maxRetrievedRounded.equals(maxComputedRounded));
			Assert.isTrue(stdRetrievedRounded.equals(stdComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The average, the minimum, the maximum and the standard deviation for the
	//				  number of day passes sold per museum.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverAvgMinMaxStdDayPassesPerMuseum() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgMinMaxStdDayPassesPerMuseum((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgMinMaxStdDayPassesPerMuseum(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgDayPassesPerMuseum();
			final Double minRetrieved = this.administratorService.getMinDayPassesPerMuseum();
			final Double maxRetrieved = this.administratorService.getMaxDayPassesPerMuseum();
			final Double stdRetrieved = this.administratorService.getStdDayPassesPerMuseum();

			//Compute the values in order to check them
			final List<Integer> daypassesPerMuseum = new ArrayList<Integer>();

			for (final Museum m : this.museumService.findAll())
				daypassesPerMuseum.add(m.getDayPasses().size());

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double minRetrievedRounded = this.roundNumber(minRetrieved, 3);
			final Double maxRetrievedRounded = this.roundNumber(maxRetrieved, 3);
			final Double stdRetrievedRounded = this.roundNumber(stdRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(daypassesPerMuseum), 3);
			final Double minComputedRounded = this.roundNumber(this.computeMin(daypassesPerMuseum), 3);
			final Double maxComputedRounded = this.roundNumber(this.computeMax(daypassesPerMuseum), 3);
			final Double stdComputedRounded = this.roundNumber(this.computeStd(daypassesPerMuseum), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));
			Assert.isTrue(minRetrievedRounded.equals(minComputedRounded));
			Assert.isTrue(maxRetrievedRounded.equals(maxComputedRounded));
			Assert.isTrue(stdRetrievedRounded.equals(stdComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The ratio of sponsorships with status 'REJECTED'.
	//				* The ratio of sponsorships with status 'ACCEPTED'.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverRatioSponsorshipsStatus() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateRatioSponsorshipsStatus((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateRatioSponsorshipsStatus(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double ratioRejectedRetrieved = this.administratorService.getRatioRejectedSponsorships();
			final Double ratioAcceptedRetrieved = this.administratorService.getRatioAcceptedSponsorships();

			//Compute the values in order to check them
			final List<Sponsorship> rejectedSponsorships = new ArrayList<>();
			final List<Sponsorship> acceptedSponsorships = new ArrayList<>();
			final List<Sponsorship> sponsorships = new ArrayList<>(this.sponsorshipService.findAll());

			for (final Sponsorship s : sponsorships)
				if (s.getStatus().equals("REJECTED"))
					rejectedSponsorships.add(s);
				else if (s.getStatus().equals("ACCEPTED"))
					acceptedSponsorships.add(s);

			final Double ratioRejectedRetrievedRounded = this.roundNumber(ratioRejectedRetrieved, 3);
			final Double ratioAcceptedRetrievedRounded = this.roundNumber(ratioAcceptedRetrieved, 3);
			final Double ratioRejectedComputedRounded = this.roundNumber(this.computeRatio(rejectedSponsorships, sponsorships), 3);
			final Double ratioAcceptedComputedRounded = this.roundNumber(this.computeRatio(acceptedSponsorships, sponsorships), 3);

			Assert.isTrue(ratioRejectedRetrievedRounded.equals(ratioRejectedComputedRounded));
			Assert.isTrue(ratioAcceptedRetrievedRounded.equals(ratioAcceptedComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The listing of exhibitions that have at least 10% more sponsorships than the
	//				  average.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverExhibitions10MoreSponsorhipsThanAvg() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateExhibitions10MoreSponsorhipsThanAvg((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateExhibitions10MoreSponsorhipsThanAvg(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final List<Exhibition> exhibitionsRetrieved = new ArrayList<>(this.administratorService.getExhibitions10MoreSponsorhipsThanAvg());

			//Compute the values in order to check them

			//Compute the avg number of sponsorships in order to retrieve the list
			final List<Integer> sponsorshipsPerExhibition = new ArrayList<Integer>();

			for (final Exhibition e : this.exhibitionService.findAll())
				sponsorshipsPerExhibition.add(e.getSponsorships().size());

			final Double avgNumberSponsorships = this.computeAverage(sponsorshipsPerExhibition);

			//Get the list with more than such number

			final List<Exhibition> exhibitionsComputed = new ArrayList<Exhibition>();

			for (final Exhibition e : this.exhibitionService.findAll())
				if (e.getSponsorships().size() * 1.0 > avgNumberSponsorships * 1.1)
					exhibitionsComputed.add(e);

			Assert.isTrue(exhibitionsRetrieved.equals(exhibitionsComputed));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}
	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The top 3 guides with less exhibitions.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverTop3GuidesLessExhibitions() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateTop3GuidesLessExhibitions((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateTop3GuidesLessExhibitions(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final List<Guide> top3Retrieved = new ArrayList<Guide>(this.administratorService.getTop3GuidesLessExhibitions());
			List<Guide> top3Computed = new ArrayList<Guide>(this.guideService.findAll());

			final Comparator<Guide> lessDayPasses = new Comparator<Guide>() {

				@Override
				public int compare(final Guide o1, final Guide o2) {

					if (o1.getExhibitions().size() < o2.getExhibitions().size())
						return -1;
					else if (o1.getExhibitions().size() == o2.getExhibitions().size())
						return 0;
					else
						return 1;

				}
			};

			Collections.sort(top3Computed, lessDayPasses);
			top3Computed = top3Computed.subList(0, 3 > top3Computed.size() ? top3Computed.size() - 1 : 3);

			Assert.isTrue(top3Retrieved.equals(top3Computed));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The average and the standard deviation of the number of critiques per exhibition.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverAvgStdCritiquesPerExhibition() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgStdCritiquesPerExhibition((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgStdCritiquesPerExhibition(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgCritiquesPerExhibition();
			final Double stdRetrieved = this.administratorService.getStdCritiquesPerExhibition();

			//Compute the values in order to check them
			final List<Integer> critiquesPerExhibition = new ArrayList<Integer>();

			for (final Exhibition e : this.exhibitionService.findAll())
				critiquesPerExhibition.add(e.getCritiques().size());

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double stdRetrievedRounded = this.roundNumber(stdRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(critiquesPerExhibition), 3);
			final Double stdComputedRounded = this.roundNumber(this.computeStd(critiquesPerExhibition), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));
			Assert.isTrue(stdRetrievedRounded.equals(stdComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The average and the standard deviation of the number of artworks per exhibition.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverAvgStdArtorksPerExhibition() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgStdArtorksPerExhibition((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgStdArtorksPerExhibition(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgArtworksPerExhibition();
			final Double stdRetrieved = this.administratorService.getStdArtworksPerExhibition();

			//Compute the values in order to check them
			final List<Integer> artworksPerExhibition = new ArrayList<Integer>();

			for (final Exhibition e : this.exhibitionService.findAll())
				artworksPerExhibition.add(e.getArtworks().size());

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double stdRetrievedRounded = this.roundNumber(stdRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(artworksPerExhibition), 3);
			final Double stdComputedRounded = this.roundNumber(this.computeStd(artworksPerExhibition), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));
			Assert.isTrue(stdRetrievedRounded.equals(stdComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The ratio of banned visitors.
	//				* The ratio of banned sponsors.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverRatioBanned() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateRatioBanned((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateRatioBanned(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double ratioBVRetrieved = this.administratorService.getRatioBannedVisitors();
			final Double ratioBSRetrieved = this.administratorService.getRatioBannedSponsors();

			//Compute the values in order to check them
			final List<Sponsor> bannedSponsors = new ArrayList<>();
			final List<Visitor> bannedVisitors = new ArrayList<>();
			final List<Visitor> visitors = new ArrayList<>(this.visitorService.findAll());
			final List<Sponsor> sponsors = new ArrayList<>(this.sponsorService.findAll());

			for (final Sponsor s : sponsors)
				if (s.getUserAccount().getIsLocked())
					bannedSponsors.add(s);

			for (final Visitor v : visitors)
				if (v.getUserAccount().getIsLocked())
					bannedVisitors.add(v);

			final Double ratioBVRetrievedRounded = this.roundNumber(ratioBVRetrieved, 3);
			final Double ratioBSRetrievedRounded = this.roundNumber(ratioBSRetrieved, 3);
			final Double ratioBVComputedRounded = this.roundNumber(this.computeRatio(bannedVisitors, visitors), 3);
			final Double ratioBSComputedRounded = this.roundNumber(this.computeRatio(bannedSponsors, sponsors), 3);

			Assert.isTrue(ratioBVRetrievedRounded.equals(ratioBVComputedRounded));
			Assert.isTrue(ratioBSRetrievedRounded.equals(ratioBSComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The average and the standard deviation of the number of participants per
	//				  open group.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverAvgStdParticipantsPerOpenGroup() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateAvgStdParticipantsPerOpenGroup((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateAvgStdParticipantsPerOpenGroup(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double avgRetrieved = this.administratorService.getAvgParticipantsPerOpenGroup();
			final Double stdRetrieved = this.administratorService.getStdParticipantsPerOpenGroup();

			//Compute the values in order to check them
			final List<Integer> participantsPerOpenGroup = new ArrayList<Integer>();

			for (final Group g : this.groupService.findAll())
				if (!g.getIsClosed())
					participantsPerOpenGroup.add(g.getParticipants().size());

			final Double avgRetrievedRounded = this.roundNumber(avgRetrieved, 3);
			final Double stdRetrievedRounded = this.roundNumber(stdRetrieved, 3);
			final Double avgComputedRounded = this.roundNumber(this.computeAverage(participantsPerOpenGroup), 3);
			final Double stdComputedRounded = this.roundNumber(this.computeStd(participantsPerOpenGroup), 3);

			Assert.isTrue(avgRetrievedRounded.equals(avgComputedRounded));
			Assert.isTrue(stdRetrievedRounded.equals(stdComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The ratio of visitors who have ever created a group.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverRatioVisitorsCreatedGroups() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateRatioVisitorsCreatedGroups((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateRatioVisitorsCreatedGroups(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final Double ratioRetrieved = this.administratorService.getRatioVisitorsCreatedGroups();

			//Compute the values in order to check them
			final List<Visitor> visitorsCreatedGroup = new ArrayList<>();
			final List<Visitor> visitors = new ArrayList<>(this.visitorService.findAll());

			for (final Visitor v : visitors)
				if (v.getCreatedGroups().size() > 0)
					visitorsCreatedGroup.add(v);

			final Double ratioRetrievedRounded = this.roundNumber(ratioRetrieved, 3);
			final Double ratioComputedRounded = this.roundNumber(this.computeRatio(visitorsCreatedGroup, visitors), 3);

			Assert.isTrue(ratioRetrievedRounded.equals(ratioComputedRounded));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-001] Administrator displays the dashboard.
	// 
	// Related requirements:
	//   � REQ 22.29: An actor who is authenticated as an administrator must be able to
	//              display a dashboard with the following information:
	//				* The groups whose number of announcements is above 75% the average number of announcements per group.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverGroups75MoreAnnouncementsThanAvg() {

		final Object testingData[][] = {

			// testingData[i][0] -> usernamed of the logged Actor.
			// testingData[i][1] -> expected Exception.

			{
				// + 1) An administrator displays the statistics
				"admin", null
			}, {
				// - 2) An unauthenticated actor tries to display the statistics
				null, IllegalArgumentException.class
			}, {
				// - 3) A visitor displays the statistics
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateGroups75MoreAnnouncementsThanAvg((String) testingData[i][0], (Class<?>) testingData[i][1]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templateGroups75MoreAnnouncementsThanAvg(final String username, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in 
		this.authenticate(username);

		try {

			// 2. Display the dashboard
			final List<Group> groupsRetrieved = new ArrayList<>(this.administratorService.getGroups75MoreAnnouncementsThanAvg());

			//Compute the values in order to check them

			//Compute the avg number of announcements in order to retrieve the list
			final List<Integer> announcementsPerGroup = new ArrayList<Integer>();

			for (final Group g : this.groupService.findAll())
				announcementsPerGroup.add(g.getAnnouncements().size());

			final Double avgAnnoncementPerGroup = this.computeAverage(announcementsPerGroup);

			//Get the list with more than such number

			final List<Group> groupsComputed = new ArrayList<Group>();

			for (final Group g : this.groupService.findAll())
				if (g.getAnnouncements().size() * 1.0 > avgAnnoncementPerGroup * 1.75)
					groupsComputed.add(g);

			Assert.isTrue(groupsRetrieved.equals(groupsComputed));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	//Statistical Calculation methods

	private Double computeAverage(final Collection<? extends Number> inputCollection) {

		//v1.0 - Implemented by JA

		Double res = 0.0;

		if (!inputCollection.isEmpty()) {

			Double accSum = 0.0;

			for (final Number n : inputCollection)
				accSum += n.doubleValue();

			res = accSum / inputCollection.size();
		}

		return res;
	}

	private Double computeStd(final Collection<? extends Number> inputCollection) {

		//v1.0 - Implemented by JA

		Double res = 0.0;

		if (!inputCollection.isEmpty()) {

			Double sumSq = 0.0;
			for (final Number n : inputCollection)
				sumSq += n.doubleValue() * n.doubleValue();
			res = Math.sqrt((sumSq / inputCollection.size()) - Math.pow(this.computeAverage(inputCollection), 2));

		}

		return res;
	}

	private Double roundNumber(final Number number, final int decimalPlaces) {

		//v1.0 - Implemented by JA

		final Double dcPlacesB10 = Math.pow(10, decimalPlaces);

		final Double res = Math.round(number.doubleValue() * dcPlacesB10) / dcPlacesB10;

		return res;
	}

	private <T extends Number> T computeMax(final Collection<T> inputCollection) {

		//v1.0 - Implemented by JA

		T res = null;

		if (!inputCollection.isEmpty())
			for (final T t : inputCollection)
				if (res == null || res.longValue() < t.longValue())
					res = t;

		return res;
	}

	private <T extends Number> T computeMin(final Collection<T> inputCollection) {

		//v1.0 - Implemented by JA

		T res = null;

		if (!inputCollection.isEmpty())
			for (final T t : inputCollection)
				if (res == null || t.longValue() < res.longValue())
					res = t;

		return res;
	}

	private Double computeRatio(final Collection<?> partial, final Collection<?> complete) {

		//v1.0 - Implemented by JA

		if (partial.isEmpty() || complete.isEmpty())
			return 0.0;
		else
			return (partial.size() * 1.0) / (complete.size() * 1.0);

	}
}
