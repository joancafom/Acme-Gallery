
package services;

import java.util.ArrayList;
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

import utilities.AbstractTest;
import domain.Exhibition;
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


	// -------------------------------------------------------------------------------
	// [UC-039] Sponsorship create a sponsorship.
	// 
	// Related requirements:
	//   · REQ 13: Sponsors can request to sponsor an exhibition. These requests can be 
	//			   either ACCEPTED, PENDING, TIME_NEGOCIATION, REJECTED or EXPIRED. When 
	//			   a request is made by a sponsor, its initial status is 'PENDING'; later, 
	//			   the corresponding director can change it to 'TIME_NEGOCIATION', sending
	//			   the period in which the sponsorship will be active for that exhibition, 
	//			   or 'REJECTED'. The sponsor must now change the status to 'ACCEPTED' by 
	//			   entering a CreditCard or change it to 'REJECTED'. In case the sponsor does
	//			   not change the status before the sponsorship starting date, the request 
	//			   is flagged as EXPIRED.
	//
	//   · REQ 14: A sponsorship consists of an image that will appear whenever an exhibition 
	//			   is displayed, starting and ending dates of the campaign. The image must be 
	//			   linked to an external web page.
	//
	//   · REQ 25.1: Request to sponsor an exhibition that has not started yet
	//
	//   · REQ 25.2: List all her or his sponsorships ordered by status
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
}
