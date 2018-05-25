
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
import domain.Museum;

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

}
