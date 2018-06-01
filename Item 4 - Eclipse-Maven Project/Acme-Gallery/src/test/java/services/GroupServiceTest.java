
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
import domain.Visitor;

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

	@Autowired
	private VisitorService	visitorService;


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

	// [UC-045] Visitor delete a Group -------------------------
	// v1.0 - Alicia

	@Test
	public void driverVisitorDeleteGroup() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Group to delete.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Visitor correctly deletes a Group
				"visitor4", "group8", null
			}, {
				// 2 - (-) A Visitor tries to delete a Group that has more participants 
				"visitor1", "group5", IllegalArgumentException.class
			}, {
				// 3 - (-) A Visitor deletes a Group with meeting date in the past
				"visitor4", "group10", IllegalArgumentException.class
			}, {
				// 4 - (-) A Visitor deletes a null Group
				"visitor1", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Director deletes a Group.
				"director1", "group1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Group group = null;

			if (testingData[i][1] != null)
				group = this.groupService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateVisitorDeleteGroup((String) testingData[i][0], group, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateVisitorDeleteGroup(final String username, final Group group, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List my created groups

			final Visitor visitor = this.visitorService.findOne(super.getEntityId(username));
			final Collection<Group> oldGroups = this.groupService.getCreatedByVisitor(visitor);

			// 3. Delete a Group

			this.groupService.deleteOwner(group);

			// Flush
			this.groupService.flush();

			// 4. Check it has been deleted

			final Collection<Group> newGroups = this.groupService.getCreatedByVisitor(visitor);
			Assert.isTrue(oldGroups.contains(group));
			Assert.isTrue(!newGroups.contains(group));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// [UC-046] Visitor join a Group ---------------------------
	// v1.0 - Alicia

	@Test
	public void driverJoinGroup() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Group to join.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Visitor correctly joins a Group
				"visitor4", "group1", null
			}, {
				// 2 - (-) A Visitor tries to join a closed Group 
				"visitor7", "group2", IllegalArgumentException.class
			}, {
				// 3 - (-) A Visitor tries to join a full Group
				"visitor1", "group14", IllegalArgumentException.class
			}, {
				// 4 - (-) A Visitor tries to join a null Group
				"visitor1", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor tries to join a Group she is a participant of. 
				"visitor2", "group1", IllegalArgumentException.class
			}, {
				// 6 - (-) A Director tries to join a Group.
				"director1", "group1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Group group = null;

			if (testingData[i][1] != null)
				group = this.groupService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateJoinGroup((String) testingData[i][0], group, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateJoinGroup(final String username, final Group group, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List joined groups

			final Visitor visitor = this.visitorService.findOne(super.getEntityId(username));
			final Collection<Group> oldGroups = this.groupService.getJoinedByVisitor(visitor);

			// 3. Join a Group

			this.groupService.joinPublicGroup(group);

			// Flush
			this.groupService.flush();

			// 4. Check it has been joined

			final Collection<Group> newGroups = this.groupService.getJoinedByVisitor(visitor);
			Assert.isTrue(!oldGroups.contains(group));
			Assert.isTrue(newGroups.contains(group));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// [UC-047] Visitor quit a Group ---------------------------
	// v1.0 - Alicia

	@Test
	public void driverQuitGroup() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Group to quit.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Visitor correctly quits a Group
				"visitor2", "group1", null
			}, {
				// 2 - (-) A Visitor tries to quit a Group she created 
				"visitor1", "group1", IllegalArgumentException.class
			}, {
				// 3 - (-) A Visitor tries to quit a Group with meeting Date in the past.
				"visitor2", "group3", IllegalArgumentException.class
			}, {
				// 4 - (-) A Visitor tries to quit a null Group
				"visitor1", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor tries to quit a Group she is not a participant of. 
				"visitor4", "group1", IllegalArgumentException.class
			}, {
				// 6 - (-) A Director tries to quit a Group.
				"director1", "group1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Group group = null;

			if (testingData[i][1] != null)
				group = this.groupService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateQuitGroup((String) testingData[i][0], group, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateQuitGroup(final String username, final Group group, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List joined groups

			final Visitor visitor = this.visitorService.findOne(super.getEntityId(username));
			final Collection<Group> oldGroups = this.groupService.getJoinedByVisitor(visitor);

			// 3. Quit a Group

			this.groupService.quitGroup(group);

			// Flush
			this.groupService.flush();

			// 4. Check it has been quitted

			final Collection<Group> newGroups = this.groupService.getJoinedByVisitor(visitor);
			Assert.isTrue(oldGroups.contains(group));
			Assert.isTrue(!newGroups.contains(group));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
