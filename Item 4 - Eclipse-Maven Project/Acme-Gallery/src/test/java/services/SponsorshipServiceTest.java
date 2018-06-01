
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Exhibition;
import domain.Sponsor;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private SponsorService		sponsorService;


	// -------------------------------------------------------------------------------
	// [UC-039] Sponsorship create a sponsorship.
	// 
	// Related requirements:
	//   � REQ 13: Sponsors can request to sponsor an exhibition. These requests can be 
	//			   either ACCEPTED, PENDING, TIME_NEGOCIATION, REJECTED or EXPIRED. When 
	//			   a request is made by a sponsor, its initial status is 'PENDING'; later, 
	//			   the corresponding director can change it to 'TIME_NEGOCIATION', sending
	//			   the period in which the sponsorship will be active for that exhibition, 
	//			   or 'REJECTED'. The sponsor must now change the status to 'ACCEPTED' by 
	//			   entering a CreditCard or change it to 'REJECTED'. In case the sponsor does
	//			   not change the status before the sponsorship starting date, the request 
	//			   is flagged as EXPIRED.
	//
	//   � REQ 14: A sponsorship consists of an image that will appear whenever an exhibition 
	//			   is displayed, starting and ending dates of the campaign. The image must be 
	//			   linked to an external web page.
	//
	//   � REQ 25.1: Request to sponsor an exhibition that has not started yet
	//
	//   � REQ 25.2: List all her or his sponsorships ordered by status
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverCreateSponsorship() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the banner of the sponsorship.
		// testingData[i][2] -> the link of the sponsorship.
		// testingData[i][3] -> the exhibition the sponsorship is for.
		// testingData[i][4] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A sponsor successfully creates a pending sponsorship for an exhibition that has not started yet.
				"sponsor1", "http://www.tinyurl.com/kittenDP", "http://www.charlie.xcx/buy/VroomEP", "exhibition4", null
			}, {
				// - 2) A sponsor tries to create a pending sponsorship for an exhibition that has already started.
				"sponsor3", "http://www.tinyurl.com/kittenDP", "http://www.charlie.xcx/buy/VroomEP", "exhibition14", IllegalArgumentException.class
			}, {
				// - 3) A sponsor tries to create a pending sponsorship with an invalid banner.
				"sponsor2", "invalidURL", "http://www.charlie.xcx/buy/VroomEP", "exhibition4", ConstraintViolationException.class
			}, {
				// - 4) A sponsor tries to create a pending sponsorship with an invalid link.
				"sponsor1", "http://www.tinyurl.com/kittenDP", "invalidURL", "exhibition4", ConstraintViolationException.class
			}, {
				// - 5) A visitor tries to create a pending sponsorship.
				"visitor1", "http://www.tinyurl.com/kittenDP", "http://www.charlie.xcx/buy/VroomEP", "exhibition4", IllegalArgumentException.class
			}
		};

		Exhibition exhibition;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][3] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][3]));
			else
				exhibition = null;

			this.startTransaction();

			this.templateCreateSponsorship((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], exhibition, (Class<?>) testingData[i][4]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateCreateSponsorship(final String performer, final String banner, final String link, final Exhibition exhibition, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			//1. Get the list of the current Sponsorships of the sponsor

			List<Sponsorship> sponsorshipsBefore = null;

			if (performer.contains("sponsor"))
				sponsorshipsBefore = new ArrayList<>(this.sponsorshipService.getBySponsorPrincipal());

			// 1. Create a new Sponsorship
			final Sponsorship sponsorship = this.sponsorshipService.create(exhibition);

			sponsorship.setBanner(banner);
			sponsorship.setLink(link);

			final Sponsorship savedSponsorship = this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();

			//3. Get the list of the Sponsorships of the sponsor
			final List<Sponsorship> sponsorshipsAfter = new ArrayList<>(this.sponsorshipService.getBySponsorPrincipal());

			Assert.isTrue(sponsorshipsAfter.contains(savedSponsorship));
			Assert.isTrue(!sponsorshipsBefore.contains(savedSponsorship));
			Assert.isTrue(sponsorshipsAfter.size() == sponsorshipsBefore.size() + 1);
			Assert.isTrue(sponsorshipsAfter.containsAll(sponsorshipsBefore));

			//Assert that now the exhibition contains the sponsorship
			final Exhibition exhibitionAfter = this.exhibitionService.findOne(savedSponsorship.getExhibition().getId());
			Assert.isTrue(exhibitionAfter.getSponsorships().contains(savedSponsorship));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// [UC-034] Director negotiate time of a Sponsorship -------
	// v1.0 - Alicia

	@Test
	public void driverNegotiateTime() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Sponsorship to edit.
		// testingData[i][2] -> status of the Sponsorship.
		// testingData[i][3] -> banner of the Sponsorship.
		// testingData[i][4] -> link of the Sponsorship.
		// testingData[i][5] -> sponsor of the Sponsorship.
		// testingData[i][6] -> exhibition of the Sponsorship.
		// testingData[i][7] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Director correctly negotiates the time
				"director1", "sponsorship5", null, null, null, null, null, null
			}, {
				// 2 - (-) A Director tries to negotiate the time of a non-pending Sponsorship
				"director1", "sponsorship1", null, null, null, null, null, IllegalArgumentException.class
			}, {
				// 3 - (-) A Director tries to edit more properties of the Sponsorship
				"director1", "sponsorship5", "ACCEPTED", "http://www.google.es", "http://www.google.es", "sponsor2", "exhibition1", IllegalArgumentException.class
			}, {
				// 4 - (-) A Director tries to negotiate dates out of the range of the exhibition
				"director1", "sponsorship5", null, null, null, null, null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Director tries to negotiate dates in the past
				"director1", "sponsorship5", null, null, null, null, null, IllegalArgumentException.class
			}, {
				// 6 - (-) A Visitor tries to negotiate the time.
				"visitor1", "sponsorship5", null, null, null, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final Sponsorship sponsorship = this.sponsorshipService.findOne(super.getEntityId((String) testingData[i][1]));

			final Sponsor sponsor = null;

			if (testingData[i][6] != null)
				this.sponsorService.findOne(super.getEntityId((String) testingData[i][5]));

			final Exhibition exhibition = null;

			if (testingData[i][6] != null)
				this.exhibitionService.findOne(super.getEntityId((String) testingData[i][6]));

			Date startingDate = null;
			Date endingDate = null;

			if (i < 3 || i == 5) {
				final LocalDate sDate = new LocalDate(sponsorship.getStartingDate());
				startingDate = sDate.plusMonths(3).toDate();
				endingDate = sDate.plusMonths(4).toDate();
			}

			if (i == 3) {
				final LocalDate sDate = new LocalDate(sponsorship.getStartingDate());
				startingDate = sDate.plusMonths(6).toDate();
				final LocalDate eDate = new LocalDate(sponsorship.getEndingDate());
				endingDate = eDate.plusMonths(2).toDate();
			}

			if (i == 4) {
				final LocalDate sDate = new LocalDate(sponsorship.getStartingDate());
				startingDate = sDate.minusDays(14).toDate();
				endingDate = sDate.plusDays(10).toDate();
			}

			this.startTransaction();

			this.templateNegotiateTime((String) testingData[i][0], sponsorship, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], sponsor, exhibition, startingDate, endingDate, (Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateNegotiateTime(final String username, final Sponsorship sponsorship, final String status, final String banner, final String link, final Sponsor sponsor, final Exhibition exhibition, final Date startingDate, final Date endingDate,
		final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Negotiate the time

			if (status != null)
				sponsorship.setStatus(status);
			else
				sponsorship.setStatus("TIME_NEGOTIATION");

			if (banner != null)
				sponsorship.setBanner(banner);

			if (link != null)
				sponsorship.setLink(link);

			if (sponsor != null)
				sponsorship.setSponsor(sponsor);

			if (exhibition != null)
				sponsorship.setExhibition(exhibition);

			if (startingDate != null)
				sponsorship.setStartingDate(startingDate);

			if (endingDate != null)
				sponsorship.setEndingDate(endingDate);

			final Sponsorship savedSponsorship = this.sponsorshipService.updateStatus(sponsorship);

			// Flush
			this.sponsorshipService.flush();

			// 3. Check that it has been updated

			Assert.notNull(savedSponsorship.getStartingDate());
			Assert.notNull(savedSponsorship.getEndingDate());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// [UC-035] Director reject a Sponsorship ------------------
	// v1.0 - Alicia

	@Test
	public void driverRejectSponsorship() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Sponsorship to edit.
		// testingData[i][2] -> status of the Sponsorship.
		// testingData[i][3] -> banner of the Sponsorship.
		// testingData[i][4] -> link of the Sponsorship.
		// testingData[i][5] -> sponsor of the Sponsorship.
		// testingData[i][6] -> exhibition of the Sponsorship.
		// testingData[i][7] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Director correctly rejects a Sponsorship
				"director1", "sponsorship5", null, null, null, null, null, null
			}, {
				// 2 - (-) A Director tries to reject a non-pending Sponsorship
				"director1", "sponsorship1", null, null, null, null, null, IllegalArgumentException.class
			}, {
				// 3 - (-) A Director tries to edit more properties of the Sponsorship
				"director1", "sponsorship5", "ACCEPTED", "http://www.google.es", "http://www.google.es", "sponsor2", "exhibition1", IllegalArgumentException.class
			}, {
				// 4 - (-) A Director tries to reject a null Sponsorship
				"director1", null, null, null, null, null, null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor tries to reject a Sponsorship
				"visitor1", "sponsorship5", null, null, null, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Sponsorship sponsorship = null;

			if (testingData[i][1] != null)
				sponsorship = this.sponsorshipService.findOne(super.getEntityId((String) testingData[i][1]));

			final Sponsor sponsor = null;

			if (testingData[i][6] != null)
				this.sponsorService.findOne(super.getEntityId((String) testingData[i][5]));

			final Exhibition exhibition = null;

			if (testingData[i][6] != null)
				this.exhibitionService.findOne(super.getEntityId((String) testingData[i][6]));

			Date startingDate = null;
			Date endingDate = null;

			if (i == 2) {
				final LocalDate sDate = new LocalDate(sponsorship.getStartingDate());
				startingDate = sDate.plusMonths(3).toDate();
				endingDate = sDate.plusMonths(4).toDate();
			}

			this.startTransaction();

			this.templateRejectSponsorship((String) testingData[i][0], sponsorship, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], sponsor, exhibition, startingDate, endingDate, (Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateRejectSponsorship(final String username, final Sponsorship sponsorship, final String status, final String banner, final String link, final Sponsor sponsor, final Exhibition exhibition, final Date startingDate,
		final Date endingDate, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Negotiate the time

			if (status != null && sponsorship != null)
				sponsorship.setStatus(status);
			else if (sponsorship != null)
				sponsorship.setStatus("REJECTED");

			if (banner != null)
				sponsorship.setBanner(banner);

			if (link != null)
				sponsorship.setLink(link);

			if (sponsor != null)
				sponsorship.setSponsor(sponsor);

			if (exhibition != null)
				sponsorship.setExhibition(exhibition);

			if (startingDate != null)
				sponsorship.setStartingDate(startingDate);

			if (endingDate != null)
				sponsorship.setEndingDate(endingDate);

			this.sponsorshipService.updateStatus(sponsorship);

			// Flush
			this.sponsorshipService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}