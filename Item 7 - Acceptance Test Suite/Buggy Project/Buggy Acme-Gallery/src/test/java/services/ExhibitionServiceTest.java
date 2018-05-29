
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
import domain.Exhibition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ExhibitionServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private ExhibitionService	exhibitionService;

	//Helping Services

	@PersistenceContext
	private EntityManager		entityManager;


	/*
	 * [UC-002] - Search for exhibitions
	 * 1. Log in to the system
	 * 2. Get to the exhibition browser
	 * 3. List your search results
	 * 4. Display an exhibition
	 * 
	 * v1.0 - josembell
	 */
	@Test
	public void driverSearchExhibitions() {

		final Object testingData[][] = {
			{
				// 1 - (+) Un usuario no identificado busca exhibiciones
				null, "Van Gogh", "exhibition1", null
			}, {
				// 2 - (+) Un visitor busca exhibiciones
				"visitor1", "Van Gogh", "exhibition1", null
			}, {
				// 3 - (+) Un administrador busca exhibiciones
				"admin", "Van Gogh", "exhibition1", null
			}, {
				// 4 - (+) Un sponsor busca exhibiciones
				"sponsor1", "Van Gogh", "exhibition1", null
			}, {
				// 5 - (+) Un director busca exhibiciones
				"director1", "Van Gogh", "exhibition1", null
			}, {
				// 6 - (+) Un guia busca exhibiciones
				"guide1", "Van Gogh", "exhibition1", null
			}, {
				// 7 - (+) Un critic busca exhibiciones
				"critic1", "Van Gogh", "exhibition1", null
			}, {
				/* 8 - (-) Un user displayea una exhibicion null */
				null, "Van Gogh", null, NullPointerException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Exhibition exhibition = null;
			if (testingData[i][2] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][2]));
			this.startTransaction();

			this.templateSearchExhibitions((String) testingData[i][0], (String) testingData[i][1], exhibition, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	/* v1.0 - josembell */
	private void templateSearchExhibitions(final String username, final String keyword, final Exhibition exhibition, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Authenticate */
		this.authenticate(username);

		try {
			/* 2. Search by keyword */
			final Collection<Exhibition> searchResults = this.exhibitionService.getByKeyword(keyword);

			/* 3. Display -> entra por parametros */
			Integer exId = null;
			if (exhibition != null)
				exId = exhibition.getId();

			final Exhibition exDisplay = this.exhibitionService.findOne(exId);
			Assert.notNull(searchResults.contains(exDisplay));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
