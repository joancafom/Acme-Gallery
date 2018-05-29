
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
import domain.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class GroupServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private GroupService	groupService;

	//Helping Services

	@PersistenceContext
	private EntityManager	entityManager;


	/*
	 * [UC-011] - Administrator delete group
	 * 1. Log in as administrator
	 * 2. List the taboo groups
	 * 3. Delete a group
	 * 
	 * v1.0 - josembell
	 * v2.0 - Alicia
	 */
	@Test
	public void driverDeleteGroup() {

		final Object testingData[][] = {
			{
				// 1 - (+) Un admin elimina un grupo
				"admin", "group10", null
			}, {
				/* 2 - (-) Un usuario no identificado elimina un grupo */
				null, "group10", IllegalArgumentException.class
			}, {
				/* 3 - (-) Un visitor elimina un grupo */
				"visitor1", "group10", IllegalArgumentException.class
			}, {
				/* 4 - (-) Un sponsor elimina un grupo */
				"sponsor1", "group10", IllegalArgumentException.class
			}, {
				/* 5 - (-) Un guide elimina un grupo */
				"guide1", "group10", IllegalArgumentException.class
			}, {
				/* 6 - (-) Un critic elimina un grupo */
				"critic1", "group10", IllegalArgumentException.class
			}, {
				/* 7 - (-) Un director elimina un grupo */
				"director1", "group10", IllegalArgumentException.class
			}, {
				/* 8 - (-) Un admin elimina un grupo null */
				"admin", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Group group = null;
			if (testingData[i][1] != null)
				group = this.groupService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			this.templateDeleteGroup((String) testingData[i][0], group, (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	// v2.0 - Alicia
	protected void templateDeleteGroup(final String username, final Group group, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Authenticate */
		this.authenticate(username);

		try {
			/* 2. List taboo groups */
			final Collection<Group> tabooedGroups = this.groupService.findTabooed();

			/* 3. Check that the group to delete is in the list */
			Assert.isTrue(tabooedGroups.contains(group));

			/* 4. Delete it */

			this.groupService.delete(group);

			/* Flush */
			this.groupService.flush();

			/* 5. Check that the group is not in the BD anymore */
			Assert.isTrue(!this.groupService.findAll().contains(group));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
