
package services;

import java.util.Collection;
import java.util.Date;

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
import domain.Critique;
import domain.Exhibition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CritiqueServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private CritiqueService		critiqueService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private ExhibitionService	exhibitionService;


	// [UC-019] Critic create Critique ---------------
	// v1.0 - Alicia

	@Test
	public void driverCreateCritique() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> title of the Critique.
		// testingData[i][2] -> description of the Critique.
		// testingData[i][3] -> score of the Critique.
		// testingData[i][4] -> creationDate of the Critique.
		// testingData[i][5] -> exhibition of the Critique.
		// testingData[i][6] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Critic correctly creates a new Critique.
				"critic2", "testTitle", "testDescription", 4, null, "exhibition2", null
			}, {
				// 2 - (-) A Critic creates a Critique for an already started exhibition.
				"critic1", "testTitle", "testDescription", 4, null, "exhibition12", IllegalArgumentException.class
			}, {
				// 3 - (-) A Critic creates a Critique for an already started exhibition indicating a past date for the Critique.
				"critic1", "testTitle", "testDescription", 4, "past", "exhibition12", IllegalArgumentException.class
			}, {
				// 4 - (-) A Critic creates a Critique with a future date.
				"critic2", "testTitle", "testDescription", 4, "future", "exhibition2", ConstraintViolationException.class
			}, {
				// 5 - (-) A Visitor creates a Critique.
				"visitor1", "testTitle", "testDescription", 4, null, "exhibition2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final Exhibition exhibition = this.exhibitionService.findOne(super.getEntityId((String) testingData[i][5]));

			Date creationDate = null;

			if (testingData[i][4] != null && i == 2) {
				final LocalDate local = new LocalDate(exhibition.getStartingDate());
				creationDate = local.minusMonths(2).toDate();
			}

			if (testingData[i][4] != null && i == 3) {
				final LocalDate local = new LocalDate(exhibition.getStartingDate());
				creationDate = local.minusDays(10).toDate();
			}

			this.startTransaction();

			this.templateCreateCritique((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], creationDate, exhibition, (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateCreateCritique(final String username, final String title, final String description, final Integer score, final Date creationDate, final Exhibition exhibition, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Create a Critique

			final Critique createdCritique = this.critiqueService.create(exhibition);

			if (creationDate != null)
				createdCritique.setCreationDate(creationDate);

			createdCritique.setTitle(title);
			createdCritique.setDescription(description);
			createdCritique.setScore(score);

			final Critique savedCritique = this.critiqueService.saveCreate(createdCritique);

			// Flush
			this.critiqueService.flush();

			if (creationDate != null)
				Assert.isTrue(!savedCritique.getCreationDate().equals(creationDate));

			// 3. List Critiques (display exhibition)

			final Collection<Critique> critiques = this.critiqueService.findAllByExhibition(exhibition);
			Assert.isTrue(critiques.contains(savedCritique));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

}
