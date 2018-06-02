
package services;

import java.util.Collection;

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
import org.springframework.validation.BeanPropertyBindingResult;

import utilities.AbstractTest;
import domain.Artwork;
import domain.Exhibition;
import domain.Guide;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ArtworkServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private ArtworkService		artworkService;

	//Helping Services

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private GuideService		guideService;

	@Autowired
	private ExhibitionService	exhibitionService;


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

	// -------------------------------------------------------------------------------
	// [UC-053] Guide create artwork.
	// 
	// Related requirements:
	//   � REQ 11: An exhibition is led by guides. In order to help visitors, each guide
	//			   can create some artworks. For every artwork the system must store: the
	//			   title, a photograph, an optional name of the creator, a short remark,
	//			   an optional year and if it is a highlight of its exhibition or not.
	//			   Artworks can be saved in draft mode, which allows it and its fellow
	//			   guides to modify it later. Draft artworks should never be shown to 
	// 			   visitors and must only be modified by guides of the corresponding museum.
	//			   Final mode freezes them forever.
	//
	//   � REQ 26.4:  Create an artwork for an exhibition she or he works for. Artworks can be saved in
	//				  draft mode or final mode, which freezes them forever.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverCreateArtwork() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the title of the artwork.
		// testingData[i][2] -> the photograph of the artwork.
		// testingData[i][3] -> the creatorName of the artwork.
		// testingData[i][4] -> the remark of the artwork.
		// testingData[i][5] -> the year of the artwork.
		// testingData[i][6] -> if the Artwork is final or not
		// testingData[i][7] -> if the Artwork is a highlight or not.
		// testingData[i][8] -> the exhibition where to add the artwork
		// testingData[i][9] -> the expected exception.

		final LocalDate now = new LocalDate();

		final Object testingData[][] = {
			{
				// + 1) A guide successfully creates an artwork for one of the exhibitions she or he leads (all fields)
				"guide1", "Test Artwork", "http://www.tinyurl.com/kittenDP", "Creator Test", "Remark Test", now.getYear(), true, true, "exhibition4", null
			}, {
				// + 2) A guide successfully creates an artwork for one of the exhibitions she or he leads (without optional fields)
				"guide2", "Test Artwork", "http://www.tinyurl.com/kittenDP", null, "Remark Test", null, true, true, "exhibition8", null
			}, {
				// - 3) A guide tries to create an artwork for an exhibition she or he does not lead
				"guide1", "Test Artwork", "http://www.tinyurl.com/kittenDP", null, "Remark Test", null, true, true, "exhibition8", IllegalArgumentException.class
			}, {
				// - 4) A guide tries to create an artwork with XSS in its title
				"guide1", "Test <script>alert('hacked!');</script> Artwork", "http://www.tinyurl.com/kittenDP", "Creator Test", "Remark Test", 1990, true, true, "exhibition4", ConstraintViolationException.class
			}, {
				// - 5) A guide tries to create an artwork with a future year
				"guide1", "Test Artwork", "http://www.tinyurl.com/kittenDP", "Creator Test", "Remark Test", now.getYear() + 1, true, true, "exhibition4", IllegalArgumentException.class
			}
		};

		Exhibition exhibition;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][8] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][8]));
			else
				exhibition = null;

			this.startTransaction();

			this.templateCreateArtwork((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Boolean) testingData[i][6],
				(Boolean) testingData[i][7], exhibition, (Class<?>) testingData[i][9]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateCreateArtwork(final String performer, final String title, final String photograph, final String creatorName, final String remark, final Integer year, final Boolean isFinal, final Boolean isHighlight, final Exhibition exhibition,
		final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Create a new Artwork
			final Artwork artwork = this.artworkService.create(exhibition);

			artwork.setTitle(title);
			artwork.setPhotograph(photograph);
			artwork.setCreatorName(creatorName);
			artwork.setRemark(remark);
			artwork.setYear(year);
			artwork.setIsFinal(isFinal);
			artwork.setIsHighlight(isHighlight);

			final Artwork savedArtwork = this.artworkService.save(artwork);
			this.artworkService.flush();
			this.exhibitionService.flush();

			//Assert that now the exhibition contains the product
			final Exhibition exhibitionAfter = this.exhibitionService.findOne(exhibition.getId());
			Assert.isTrue(exhibitionAfter.getArtworks().contains(savedArtwork));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-054] Guide edit artwork.
	// 
	// Related requirements:
	//   � REQ 11: An exhibition is led by guides. In order to help visitors, each guide
	//			   can create some artworks. For every artwork the system must store: the
	//			   title, a photograph, an optional name of the creator, a short remark,
	//			   an optional year and if it is a highlight of its exhibition or not.
	//			   Artworks can be saved in draft mode, which allows it and its fellow
	//			   guides to modify it later. Draft artworks should never be shown to 
	// 			   visitors and must only be modified by guides of the corresponding museum.
	//			   Final mode freezes them forever.
	//
	//   � REQ 26.5: Edit an artwork of an exhibition she or he works for, if it's in draft mode.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverEditArtwork() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the title of the artwork.
		// testingData[i][2] -> the photograph of the artwork.
		// testingData[i][3] -> the creatorName of the artwork.
		// testingData[i][4] -> the remark of the artwork.
		// testingData[i][5] -> the year of the artwork.
		// testingData[i][6] -> if the Artwork is final or not
		// testingData[i][7] -> if the Artwork is a highlight or not.
		// testingData[i][8] -> the artwork to edit
		// testingData[i][9] -> the expected exception.

		final LocalDate now = new LocalDate();

		final Object testingData[][] = {
			{
				// + 1) A guide successfully edits a draft artwork for one of the exhibitions she or he leads (all fields)
				"guide3", "Test Artwork", "http://www.tinyurl.com/kittenDP", "Creator Test", "Remark Test", now.getYear(), true, true, "artwork8", null
			}, {
				// + 2) A guide successfully edits a draft artwork for one of the exhibitions she or he leads (without optional fields)
				"guide3", "Test Artwork", "http://www.tinyurl.com/kittenDP", null, "Remark Test", null, true, true, "artwork8", null
			}, {
				// - 3) A guide tries to edit a draft artwork for an exhibition she or he does not lead
				"guide1", "Test Artwork", "http://www.tinyurl.com/kittenDP", null, "Remark Test", null, true, true, "artwork8", IllegalArgumentException.class
			}, {
				// - 4) A guide tries to edit a final artwork for one of the exhibitions she or he leads
				"guide3", "Test <script>alert('hacked!');</script> Artwork", "http://www.tinyurl.com/kittenDP", "Creator Test", "Remark Test", 1990, true, true, "artwork8", ConstraintViolationException.class
			}, {
				// - 5) A guide tries to create an artwork with a future year
				"guide3", "Test Artwork", "http://www.tinyurl.com/kittenDP", "Creator Test", "Remark Test", now.getYear() + 1, true, true, "artwork8", IllegalArgumentException.class
			}
		};

		Artwork artwork;

		for (int i = 0; i < testingData.length; i++) {

			System.err.println(i + 1);

			if (testingData[i][8] != null)
				artwork = this.artworkService.findOne(this.getEntityId((String) testingData[i][8]));
			else
				artwork = null;

			this.startTransaction();

			this.templateEditArtwork((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Boolean) testingData[i][6],
				(Boolean) testingData[i][7], artwork, (Class<?>) testingData[i][9]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateEditArtwork(final String performer, final String title, final String photograph, final String creatorName, final String remark, final Integer year, final Boolean isFinal, final Boolean isHighlight, final Artwork artwork,
		final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			final Artwork artworkToUpdate = this.getCopy(artwork);

			// 1. Edit the artwork that is given via parameters
			artworkToUpdate.setCreatorName(creatorName);
			artworkToUpdate.setIsFinal(isFinal);
			artworkToUpdate.setIsHighlight(isHighlight);
			artworkToUpdate.setPhotograph(photograph);
			artworkToUpdate.setRemark(remark);
			artworkToUpdate.setTitle(title);
			artworkToUpdate.setYear(year);

			//The artwork to be updated is passed through a reconstruct as in real life
			final Artwork reconstructedArtwork = this.artworkService.reconstruct(artworkToUpdate, new BeanPropertyBindingResult(artworkToUpdate, ""));

			final Artwork updatedArtwork = this.artworkService.save(reconstructedArtwork);
			this.artworkService.flush();

			//Assert that the updated artwork appears in the exhibition
			Artwork foundArtwork = null;

			for (final Artwork a : this.exhibitionService.findOne(artwork.getExhibition().getId()).getArtworks())
				if (a.equals(updatedArtwork)) {
					foundArtwork = a;
					break;
				}

			Assert.notNull(foundArtwork);

			//Assert that the fields were updated
			Assert.isTrue(foundArtwork.getIsFinal() == isFinal);
			Assert.isTrue(foundArtwork.getIsHighlight() == isHighlight);

			if (foundArtwork.getCreatorName() != null)
				Assert.isTrue(foundArtwork.getCreatorName().equals(creatorName));
			else
				Assert.isNull(creatorName);

			if (foundArtwork.getYear() != null)
				Assert.isTrue(foundArtwork.getYear().equals(year));
			else
				Assert.isNull(year);

			Assert.isTrue(foundArtwork.getExhibition().equals(artworkToUpdate.getExhibition()));
			Assert.isTrue(foundArtwork.getPhotograph().equals(photograph));
			Assert.isTrue(foundArtwork.getRemark().equals(remark));
			Assert.isTrue(foundArtwork.getTitle().equals(title));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	//Auxiliary Methods
	private Artwork getCopy(final Artwork artwork) {

		final Artwork res = new Artwork();

		res.setId(artwork.getId());
		res.setVersion(artwork.getVersion());
		res.setExhibition(artwork.getExhibition());
		res.setCreatorName(artwork.getCreatorName());
		res.setIsFinal(artwork.getIsFinal());
		res.setIsHighlight(artwork.getIsHighlight());
		res.setPhotograph(artwork.getPhotograph());
		res.setRemark(artwork.getRemark());
		res.setTitle(artwork.getTitle());
		res.setYear(artwork.getYear());

		return res;
	}
}
