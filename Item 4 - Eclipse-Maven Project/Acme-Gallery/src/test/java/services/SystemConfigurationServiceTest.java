
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private SystemConfigurationService	SystemConfigurationService;

	//Helping Services

	@PersistenceContext
	private EntityManager				entityManager;


	/*
	 * [UC-015] - Add a taboo word
	 * 1. Log in as administrator
	 * 2. List the taboo groups
	 * 3. Delete a group
	 * 
	 * v1.0 - josembell
	 * 
	 * NO FUNCIONA!!!!!
	 */
	@Test
	public void driverAddTabooWord() {

		final Object testingData[][] = {
			{
				/* + 1. Un admin añade una taboo word válida */
				"admin", "gogh", null
			}, {
				/* - 2. Un usuario no identificado añade una taboo word */
				null, "gogh", IllegalArgumentException.class
			}, {
				/* - 3. Un visitor añade una taboo word */
				"visitor1", "gogh", IllegalArgumentException.class
			}, {
				/* - 4. Un guide añade una taboo word */
				"guide1", "gogh", IllegalArgumentException.class
			}, {
				/* - 5. Un sponsor añade una taboo word */
				"sponsor1", "gogh", IllegalArgumentException.class
			}, {
				/* - 6. Un director añade una taboo word */
				"director1", "gogh", IllegalArgumentException.class
			}, {
				/* - 7. Un critic añade una taboo word */
				"critic1", "gogh", IllegalArgumentException.class
			}, {
				/* - 8. Un admin añade una taboo word null */
				"admin", null, IllegalArgumentException.class
			}, {
				/* - 9. Un admin añade una taboo word que ya estaba puesta */
				"admin", "sex", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			//System.out.println(i);
			this.templateAddTabooWord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* 1.0 - josembell */
	protected void templateAddTabooWord(final String username, final String tabooWord, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 2. display the system configuration -> list of taboo words */
			final Collection<String> tabooWordsBefore = this.SystemConfigurationService.getTabooWords();
			final int numTabooWordsBefore = tabooWordsBefore.size();

			/* 3. add the new taboo word */
			this.SystemConfigurationService.addTabooWord(tabooWord);

			/* 4. check that the new taboo word is in the list */
			final Collection<String> tabooWordsNow = this.SystemConfigurationService.getTabooWords();
			final int numTabooWordsNow = tabooWordsNow.size();

			Assert.isTrue(numTabooWordsBefore + 1 == numTabooWordsNow);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}

}
