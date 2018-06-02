
package services;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Group;
import domain.Museum;
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
	private MuseumService	museumService;

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

	// [UC-058] Visitor create a Group --------------------
	// v1.0 - Alicia

	@Test
	public void driverCreateAnnouncement() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> name of the Group.
		// testingData[i][2] -> description of the Group.
		// testingData[i][3] -> creationMoment of the Group.
		// testingData[i][4] -> maxParticipants of the Group.
		// testingData[i][5] -> meetingDate of the Group.
		// testingData[i][6] -> isClosed of the Group.
		// testingData[i][7] -> containsTaboo of the Group.
		// testingData[i][8] -> creator of the Group.
		// testingData[i][9] -> museum of the Group.
		// testingData[i][10] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Visitor correctly creates a new Group.
				"visitor1", "testName", "testDescription", null, 10, new LocalDate().plusMonths(4).toDate(), true, null, null, "museum1", null
			}, {
				// 2 - (+) A Visitor creates a Group trying to edit containsTaboo and creationMoment
				"visitor1", "testName", "sexDescription", new LocalDate().plusMonths(2).toDate(), 10, new LocalDate().plusMonths(4).toDate(), false, false, null, "museum1", null
			}, {
				// 3 - (-) A Visitor creates a Group saying she is not the creator.
				"visitor1", "testName", "testDescription", null, 10, new LocalDate().plusMonths(4).toDate(), true, null, "visitor2", "museum1", IllegalArgumentException.class
			}, {
				// 4 - (-) A Visitor creates Group for a null museum.
				"visitor1", "testName", "testDescription", null, 10, new LocalDate().plusMonths(4).toDate(), true, null, null, null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Director creates an Group.
				"director1", "testName", "testDescription", null, 10, new LocalDate().plusMonths(4).toDate(), true, null, null, "museum1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Visitor creator = null;

			if (testingData[i][8] != null)
				creator = this.visitorService.findOne(super.getEntityId((String) testingData[i][8]));

			Museum museum = null;

			if (testingData[i][9] != null)
				museum = this.museumService.findOne(super.getEntityId((String) testingData[i][9]));

			this.startTransaction();

			this.templateCreateGroup((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (Integer) testingData[i][4], (Date) testingData[i][5], (Boolean) testingData[i][6],
				(Boolean) testingData[i][7], creator, museum, (Class<?>) testingData[i][10]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateCreateGroup(final String username, final String name, final String description, final Date creationMoment, final Integer maxParticipants, final Date meetingDate, final Boolean isClosed, final Boolean containsTaboo,
		final Visitor creator, final Museum museum, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Create a Group

			final Visitor visitor = this.visitorService.findOne(super.getEntityId(username));
			final Collection<Group> oldGroups = this.groupService.getCreatedByVisitor(visitor);

			final Group createdGroup = this.groupService.create();

			if (creationMoment != null)
				createdGroup.setCreationMoment(creationMoment);

			if (containsTaboo != null)
				createdGroup.setContainsTaboo(containsTaboo);

			if (creator != null)
				createdGroup.setCreator(creator);

			createdGroup.setName(name);
			createdGroup.setDescription(description);
			createdGroup.setMaxParticipants(maxParticipants);
			createdGroup.setMeetingDate(meetingDate);
			createdGroup.setIsClosed(isClosed);
			createdGroup.setMuseum(museum);

			final Group savedGroup = this.groupService.saveCreate(createdGroup);

			// Flush
			this.groupService.flush();

			// 3. Check it has been correctly created

			if (containsTaboo != null)
				Assert.isTrue(savedGroup.isContainsTaboo() != containsTaboo);

			if (creationMoment != null)
				Assert.isTrue(!savedGroup.getCreationMoment().equals(creationMoment));

			final Collection<Group> newGroups = this.groupService.getCreatedByVisitor(visitor);
			Assert.isTrue(!oldGroups.contains(savedGroup));
			Assert.isTrue(newGroups.contains(savedGroup));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
