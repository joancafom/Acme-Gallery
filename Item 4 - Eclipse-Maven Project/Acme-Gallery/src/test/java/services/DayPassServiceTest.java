
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.DayPass;
import domain.Exhibition;
import domain.Museum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DayPassServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private DayPassService				dayPassService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager				entityManager;

	@Autowired
	private ExhibitionService			exhibitionService;

	@Autowired
	private MuseumService				museumService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// -------------------------------------------------------------------------------
	// [UC-043] Visitor buy a day pass for an exhibition.
	// 
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverBuyDayPassForExhibition() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the visitDate of the daypass.
		// testingData[i][2] -> the holdername of the creditcard.
		// testingData[i][3] -> the brandname of the creditcard.
		// testingData[i][4] -> the number of the creditcard.
		// testingData[i][5] -> the CVV of the creditcard.
		// testingData[i][6] -> the expiration month of the creditcard.
		// testingData[i][7] -> the expiration year of the creditcard.
		// testingData[i][8] -> the exhibition to buy the daypass for.
		// testingData[i][9] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A visitor successfully buys a daypass for a private exhibition that is held in the future and has some artwork in final mode.
				"visitor1", this.formatDate("11-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition14", null
			}, {
				// + 2) A visitor successfully buys a daypass for a private exhibition that is being currently held and has some artwork in final mode.
				"visitor1", this.formatDate("12-07-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition12", null
			}, {
				// - 3) A visitor tries to buy a daypass for a private exhibition that is being held and has no artwork in final mode.
				"visitor4", this.formatDate("17-07-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition6", IllegalArgumentException.class
			}, {
				// - 4) A visitor tries to buy a daypass for a public exhibition that is held in the future and has some artwork.
				"visitor4", this.formatDate("17-07-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition2", IllegalArgumentException.class
			}, {
				// - 5) A visitor tries to buy a daypass for a private exhibition that is held in the future and has some artwork by provided wrong visit dates.
				"visitor1", this.formatDate("09-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition14", IllegalArgumentException.class
			}
		};

		Exhibition exhibition;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][8] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][8]));
			else
				exhibition = null;

			this.startTransaction();

			this.templateBuyDayPassForExhibition((String) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], exhibition, (Class<?>) testingData[i][9]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateBuyDayPassForExhibition(final String performer, final Date visitDate, final String holderName, final String brandName, final String number, final Integer CVV, final Integer expMonth, final Integer expYear,
		final Exhibition exhibition, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			//1. Get the list of the current DayPasses of the visitor

			List<DayPass> dayPassesBefore = null;

			if (performer.contains("visitor"))
				dayPassesBefore = new ArrayList<>(this.dayPassService.getByPrincipal());

			// 1. Create a new DayPass
			final DayPass dayPass = this.dayPassService.create(exhibition);
			final CreditCard creditCard = new CreditCard();

			creditCard.setBrandName(brandName);
			creditCard.setCVV(CVV);
			creditCard.setHolderName(holderName);
			creditCard.setMonth(expMonth);
			creditCard.setNumber(number);
			creditCard.setYear(expYear);

			dayPass.setCreditCard(creditCard);
			dayPass.setExhibition(exhibition);
			dayPass.setVisitDate(visitDate);

			//The dayPass goes through a reconstruct, as in real life

			final DayPass reconstructedDP = this.dayPassService.reconstruct(dayPass, new BeanPropertyBindingResult(dayPass, ""));

			final DayPass savedDayPass = this.dayPassService.saveCreateAndEdit(reconstructedDP);
			this.dayPassService.flush();

			//3. Get the list of the DayPasses of the visitor
			final List<DayPass> dayPassesAfter = new ArrayList<>(this.dayPassService.getByPrincipal());

			Assert.isTrue(dayPassesAfter.contains(savedDayPass));
			Assert.isTrue(!dayPassesBefore.contains(savedDayPass));
			Assert.isTrue(dayPassesAfter.size() == dayPassesBefore.size() + 1);
			Assert.isTrue(dayPassesAfter.containsAll(dayPassesBefore));

			//Assert that now the exhibition contains the dayPass
			final Exhibition exhibitionAfter = this.exhibitionService.findOne(exhibition.getId());
			Assert.isTrue(exhibitionAfter.getDayPasses().contains(savedDayPass));

			//Assert that the price was computed correctly

			Double price = exhibition.getPrice() + exhibition.getRoom().getMuseum().getPrice();

			price *= (1 + this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());
			price = this.truncateNumber(price, 2);

			Assert.isTrue(savedDayPass.getPrice().equals(price));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
	// -------------------------------------------------------------------------------
	// [UC-044] Visitor buy a day pass for an exhibition.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverBuyDayPassForMuseum() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the visitDate of the daypass.
		// testingData[i][2] -> the holdername of the creditcard.
		// testingData[i][3] -> the brandname of the creditcard.
		// testingData[i][4] -> the number of the creditcard.
		// testingData[i][5] -> the CVV of the creditcard.
		// testingData[i][6] -> the expiration month of the creditcard.
		// testingData[i][7] -> the expiration year of the creditcard.
		// testingData[i][8] -> the exhibition to buy the daypass for.
		// testingData[i][9] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A visitor successfully buys a daypass for a museum with a visit date in the future.
				"visitor1", this.formatDate("11-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "museum1", null
			}, {
				// + 2) A visitor successfully buys a daypass for a museum with a visit date in the present.
				"visitor1", new Date(), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "museum2", null
			}, {
				// - 3) A visitor tries to buy a daypass for a museum providing a visit date in the past
				"visitor4", this.formatDate("17-05-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "museum3", IllegalArgumentException.class
			}, {
				// - 4) A visitor tries to buy a daypass for a museum providing a null visit date
				"visitor4", null, "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "museum4", IllegalArgumentException.class
			}, {
				// - 5) A visitor tries to buy a daypass for a museum providing an invalid creditcard
				"visitor1", this.formatDate("09-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334445", 123, 12, 2020, "museum1", ConstraintViolationException.class
			}
		};

		Museum museum;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][8] != null)
				museum = this.museumService.findOne(this.getEntityId((String) testingData[i][8]));
			else
				museum = null;

			this.startTransaction();

			this.templateBuyDayPassForMuseum((String) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], museum, (Class<?>) testingData[i][9]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateBuyDayPassForMuseum(final String performer, final Date visitDate, final String holderName, final String brandName, final String number, final Integer CVV, final Integer expMonth, final Integer expYear, final Museum museum,
		final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			//1. Get the list of the current DayPasses of the visitor

			List<DayPass> dayPassesBefore = null;

			if (performer.contains("visitor"))
				dayPassesBefore = new ArrayList<>(this.dayPassService.getByPrincipal());

			// 1. Create a new DayPass
			final DayPass dayPass = this.dayPassService.create(museum);
			final CreditCard creditCard = new CreditCard();

			creditCard.setBrandName(brandName);
			creditCard.setCVV(CVV);
			creditCard.setHolderName(holderName);
			creditCard.setMonth(expMonth);
			creditCard.setNumber(number);
			creditCard.setYear(expYear);

			dayPass.setCreditCard(creditCard);
			dayPass.setMuseum(museum);
			dayPass.setVisitDate(visitDate);

			//The dayPass goes through a reconstruct, as in real life

			final DayPass reconstructedDP = this.dayPassService.reconstruct(dayPass, new BeanPropertyBindingResult(dayPass, ""));

			final DayPass savedDayPass = this.dayPassService.saveCreateAndEdit(reconstructedDP);
			this.dayPassService.flush();

			//3. Get the list of the DayPasses of the visitor
			final List<DayPass> dayPassesAfter = new ArrayList<>(this.dayPassService.getByPrincipal());

			Assert.isTrue(dayPassesAfter.contains(savedDayPass));
			Assert.isTrue(!dayPassesBefore.contains(savedDayPass));
			Assert.isTrue(dayPassesAfter.size() == dayPassesBefore.size() + 1);
			Assert.isTrue(dayPassesAfter.containsAll(dayPassesBefore));

			//Assert that now the museum contains the dayPass
			final Museum museumAfter = this.museumService.findOne(museum.getId());
			Assert.isTrue(museumAfter.getDayPasses().contains(savedDayPass));

			//Assert that the price was computed correctly

			Double price = museum.getPrice();

			price *= (1 + this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());
			price = this.truncateNumber(price, 2);

			Assert.isTrue(savedDayPass.getPrice().equals(price));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	//Auxiliary Methods

	private Date formatDate(final String date) {
		Date res;

		try {
			res = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
		} catch (final ParseException e) {
			res = null;
		}

		return res;
	}

	private Double truncateNumber(final Number number, final int decimalPlaces) {

		//v1.0 - Implemented by JA

		final Double dcPlacesB10 = Math.pow(10, decimalPlaces);

		final Double res = Math.floor(number.doubleValue() * dcPlacesB10) / dcPlacesB10;

		return res;
	}
}
