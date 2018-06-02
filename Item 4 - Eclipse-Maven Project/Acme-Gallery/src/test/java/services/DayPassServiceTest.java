
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.DayPass;
import domain.Exhibition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DayPassServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private DayPassService		dayPassService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private ExhibitionService	exhibitionService;


	// -------------------------------------------------------------------------------
	// [UC-043] Visitor buy a day pass for an exhibition.
	// 
	// Related requirements:
	//   · REQ 3: A museum sells several day passes. There are two kinds of day passes: general
	//			  and private day passes. General day passes give access to public areas of the
	//			  museum whereas private ones do also grant access to some private exhibitions.
	//			  For every day pass the system must keep track of: the price (which must be 
	//	  	      greater or equal than a symbolic quantity - that is, 1euro - ), purchase and visit
	//			  dates, the corresponding room in case it's a private exhibition and a ticker 
	//			 (following the pattern AAAA-uuuu-XXXX where "AAAA" is the unique identifier of
	//			  the museum, "uuuu" is the user name and "XXXX" are numbers).
	//
	//   · REQ 9: Exhibitions can be either public or private. A day pass must be purchased in 
	//			  order to access private exhibitions whereas public ones can be visited by all
	//			  the visitors of a museum.
	//
	//   · REQ 24.20: An actor who is authenticated as a visitor must be able to buy a day pass for
	//				  a private exhibition by entering a credit card. Day passes cannot be edited or
	//				  deleted

	//   · REQ 24.22: An actor who is authenticated as a visitor must be able to list the day passes 
	//				  that she or he has bought
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
				// + 1) A visitor successfully buys a daypass for a private exhibition that is held in the future and has some artwork.
				"visitor1", this.formatDate("11-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition14", null
			}, {
				// + 2) A visitor successfully buys a daypass for a private exhibition that is being currently held and has some artwork.
				"visitor1", this.formatDate("11-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition6", null
			}, {
				// - 3) A visitor tries to buy a daypass for a private exhibition that is being held and has no artwork.
				"visitor4", this.formatDate("17-07-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition13", null
			}, {
				// - 4) A visitor tries to buy a daypass for a public exhibition that is held in the future and has some artwork.
				"visitor4", this.formatDate("17-07-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition2", null
			}, {
				// - 5) A visitor tries to buy a daypass for a private exhibition that is held in the future and has some artwork by provided wrong visit dates.
				"visitor1", this.formatDate("11-08-2018 12:00"), "Mariano Rajoy", "Discover", "1111222233334444", 123, 12, 2020, "exhibition14", null
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

			//			final Sponsorship savedSponsorship = this.sponsorshipService.save(sponsorship);
			//			this.sponsorshipService.flush();
			//
			//			//3. Get the list of the Sponsorships of the sponsor
			//			final List<Sponsorship> sponsorshipsAfter = new ArrayList<>(this.sponsorshipService.getBySponsorPrincipal());
			//
			//			Assert.isTrue(sponsorshipsAfter.contains(savedSponsorship));
			//			Assert.isTrue(!sponsorshipsBefore.contains(savedSponsorship));
			//			Assert.isTrue(sponsorshipsAfter.size() == sponsorshipsBefore.size() + 1);
			//			Assert.isTrue(sponsorshipsAfter.containsAll(sponsorshipsBefore));
			//
			//			//Assert that now the exhibition contains the sponsorship
			//			final Exhibition exhibitionAfter = this.exhibitionService.findOne(savedSponsorship.getExhibition().getId());
			//			Assert.isTrue(exhibitionAfter.getSponsorships().contains(savedSponsorship));

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
}
