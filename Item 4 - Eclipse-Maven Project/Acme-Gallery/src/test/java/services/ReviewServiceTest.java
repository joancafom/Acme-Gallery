
package services;

import java.util.Collection;

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
import domain.Museum;
import domain.Review;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ReviewServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private ReviewService	reviewService;

	@Autowired
	private MuseumService	museumService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// [UC-013] Administrator delete Review ----------
	// v1.0 - Alicia

	@Test
	public void driverDeleteReview() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Review to remove.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) An Administrator correctly removes a taboo Review.
				"admin", "review6", null
			}, {
				// 2 - (+) An Administrator correctly removes a non-taboo Review.
				"admin", "review1", null
			}, {
				// 3 - (-) An Administrator removes a non-existing Review.
				"admin", "abcdefg", IllegalArgumentException.class
			}, {
				// 4 - (-) An Administrator removes a null Review.
				"admin", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor removes a Review.
				"visitor1", "review1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Review review = null;

			if (i < 2 || i == 4)
				review = this.reviewService.findOne(super.getEntityId((String) testingData[i][1]));

			if (i == 2)
				review = new Review();

			this.startTransaction();

			this.templateDeleteReview((String) testingData[i][0], review, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateDeleteReview(final String username, final Review review, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List Reviews
			final Collection<Review> oldTabooReviews = this.reviewService.findTabooed();
			final Collection<Review> oldNonTabooReviews = this.reviewService.findNotTabooed();
			final Collection<Review> oldAllReviews = this.reviewService.findAll();

			// 3. Remove a Review
			this.reviewService.delete(review);

			// Flush
			this.reviewService.flush();

			// 4. List again and check
			final Collection<Review> newTabooReviews = this.reviewService.findTabooed();
			final Collection<Review> newNonTabooReviews = this.reviewService.findNotTabooed();
			final Collection<Review> newAllReviews = this.reviewService.findAll();

			if (review.isContainsTaboo()) {
				Assert.isTrue(oldTabooReviews.contains(review));
				Assert.isTrue(!newTabooReviews.contains(review));

			} else if (!review.isContainsTaboo()) {
				Assert.isTrue(oldNonTabooReviews.contains(review));
				Assert.isTrue(!newNonTabooReviews.contains(review));

			}

			Assert.isTrue(oldAllReviews.contains(review));
			Assert.isTrue(!newAllReviews.contains(review));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-059] Visitor create review.
	// 
	// Related requirements:
	//   · REQ 7: Visitors that had visited the museum can write reviews about it. For every 
	//			  review, the system must keep track of a body, a scoring (going from 1 to 5)
	//			  and the moment of creation.
	//
	//   · REQ 24.23: An actor who is authenticated as a visitor must be able to:
	//				  Write a review about a museum she or he has visited.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverCreateReview() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the body of the review.
		// testingData[i][2] -> the score of the review.
		// testingData[i][3] -> the museum where to add the review
		// testingData[i][4] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A visitor successfully creates a review of a museum she or he has bought a day pass for
				"visitor1", "Test Body", 3, "museum1", null
			}, {
				// - 2) A visitor tries to create a review of a museum she or he has not bought a day pass for
				"visitor3", "Test Body", 3, "museum1", IllegalArgumentException.class
			}, {
				// - 3) A visitor tries to create a review of a museum using a negative
				"visitor1", "Test Body", -2, "museum1", ConstraintViolationException.class
			}, {
				// - 4) A visitor tries to create a review of a museum using score out of bounds
				"visitor1", "Test Body", 10, "museum1", ConstraintViolationException.class
			}, {
				// - 5) A visitor tries to create a review of a museum using XSS body
				"visitor1", "Test <script>alert('hacked!');</script>", 3, "museum1", ConstraintViolationException.class
			}
		};

		Museum museum;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][3] != null)
				museum = this.museumService.findOne(this.getEntityId((String) testingData[i][3]));
			else
				museum = null;

			this.startTransaction();

			this.templateCreateReview((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], museum, (Class<?>) testingData[i][4]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateCreateReview(final String performer, final String body, final Integer score, final Museum museum, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Create a new Review
			final Review review = this.reviewService.create(museum);

			review.setBody(body);
			review.setScore(score);

			//Pass through the reconstuct, as in real life
			final Review reconstructedReview = this.reviewService.reconstruct(review, new BeanPropertyBindingResult(review, ""));

			final Review savedReview = this.reviewService.saveCreate(reconstructedReview);
			this.reviewService.flush();
			this.museumService.flush();

			//Assert that now the museum contains the review
			final Museum museumAfter = this.museumService.findOne(museum.getId());
			Assert.isTrue(museumAfter.getReviews().contains(savedReview));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
