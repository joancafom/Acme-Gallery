
package services;

import java.util.Collection;

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
}
