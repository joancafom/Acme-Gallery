
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
import domain.Artwork;
import domain.Guide;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ArtworkServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private ArtworkService	artworkService;

	//Helping Services

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private GuideService	guideService;


	// [UC-055] Guide delete Artwork ---------------------------
	// v1.0 - Alicia

	@Test
	public void driverDeleteArtwork() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Artwork to delete.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Guide correctly deletes an Artwork
				"guide3", "artwork8", null
			}, {
				// 2 - (-) A Guide tries to delete a final Artwork
				"guide1", "artwork1", IllegalArgumentException.class
			}, {
				// 3 - (-) A Guide tries to delete an Artwork in whose exhibition she doesn't work, but in its museum she does.
				"guide1", "artwork8", IllegalArgumentException.class
			}, {
				// 4 - (-) A Guide tries to delete a null Artwork
				"guide1", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Director tries to delete an Artwork
				"director1", "artwork8", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Artwork artwork = null;

			if (testingData[i][1] != null)
				artwork = this.artworkService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateDeleteArtwork((String) testingData[i][0], artwork, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateDeleteArtwork(final String username, final Artwork artwork, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List my Artworks

			final Guide guide = this.guideService.findOne(super.getEntityId(username));
			final Collection<Artwork> oldArtworks = this.artworkService.getByGuide(guide);

			// 3. Delete an Artworks

			this.artworkService.delete(artwork);

			// Flush
			this.artworkService.flush();

			// 4. Check it has been deleted

			final Collection<Artwork> newArtworks = this.artworkService.getByGuide(guide);
			Assert.isTrue(oldArtworks.contains(artwork));
			Assert.isTrue(!newArtworks.contains(artwork));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

}
