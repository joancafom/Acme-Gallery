
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

import security.LoginService;
import utilities.AbstractTest;
import domain.Announcement;
import domain.Group;
import domain.Visitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private GroupService		groupService;

	@Autowired
	private VisitorService		visitorService;

	//Helping Services

	@PersistenceContext
	private EntityManager		entityManager;


	/*
	 * [UC-005] - Administrator delete announcement
	 * v1.0 - josembell
	 */
	@Test
	public void driverDeleteAnnouncement() {

		final Object testingData[][] = {
			{
				// 1 - (+) Un admin elimina un announcement
				"admin", "announcement2", null
			}, {
				/* 2 - (-) Un usuario no identificado elimina un announcement */
				null, "announcement2", IllegalArgumentException.class
			}, {
				/* 3 - (-) Un visitor elimina un announcement */
				"visitor1", "announcement2", IllegalArgumentException.class
			}, {
				/* 4 - (-) Un sponsor elimina un announcement */
				"sponsor1", "announcement2", IllegalArgumentException.class
			}, {
				/* 5 - (-) Un guide elimina un announcement */
				"guide1", "announcement2", IllegalArgumentException.class
			}, {
				/* 6 - (-) Un critic elimina un announcement */
				"critic1", "announcement2", IllegalArgumentException.class
			}, {
				/* 7 - (-) Un director elimina un announcement */
				"director1", "announcement1", IllegalArgumentException.class
			}, {
				/* 8 - (-) Un admin elimina un announcement null */
				"admin", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Announcement announcement = null;
			if (testingData[i][1] != null)
				announcement = this.announcementService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();

			this.templateDeleteAnnouncement((String) testingData[i][0], announcement, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateDeleteAnnouncement(final String username, final Announcement announcement, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Authenticate */
		this.authenticate(username);

		try {
			/* 2. List taboo announcements */
			final Collection<Announcement> tabooedAnnouncements = this.announcementService.findTabooed();

			/* 3. Check that the announcement to delete is in the list */
			Assert.isTrue(tabooedAnnouncements.contains(announcement));

			/* 4. Delete it */
			this.announcementService.delete(announcement);

			/* Flush */
			this.announcementService.flush();

			/* 5. Check that the announcement is not in the BD anymore */
			Assert.isTrue(!this.announcementService.findAll().contains(announcement));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-057] Visitor Stream Announcements.
	// 
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverStreamAnnouncement() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the group to subscribe/unsubscribe
		// testingData[i][2] -> null --> nothing, true --> subscribe, false --> unsubscribe
		// testingData[i][3] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A visitor successfully retrieves her or his stream of announcements
				"visitor1", null, null, null
			}, {
				// + 2) A visitor successfully retrieves her or his stream of announcements after joining a group
				"visitor5", "group1", true, null
			}, {
				// + 3) A visitor successfully retrieves her or his stream of announcements after quitting a group
				"visitor2", "group1", false, null
			}, {
				// - 4) A sponsor tries to retrieve the stream of annoucements
				"sponsor1", "group1", null, IllegalArgumentException.class
			}
		};

		Group group;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				group = this.groupService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				group = null;

			this.startTransaction();

			this.templateStreamAnnouncement((String) testingData[i][0], group, (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateStreamAnnouncement(final String performer, final Group groupToJoin, final Boolean toJoin, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			//2. If we wanted to join to a group, we proceed by doing so

			if (toJoin != null && performer.contains("visitor")) {

				if (toJoin)
					this.groupService.joinPublicGroup(groupToJoin);
				else
					this.groupService.quitGroup(groupToJoin);

				this.groupService.flush();
			}

			//3. Now, retrieve the list of Annoucements

			final Collection<Announcement> collectionR = this.announcementService.getStreamByPrincipal();

			//Assert that they all belong to some groups the visitor has joined

			final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
			final Collection<Group> joinedGroups = this.groupService.getJoinedByVisitor(visitor);

			for (final Announcement a : collectionR)
				Assert.isTrue(joinedGroups.contains(a.getGroup()));

			//If we have recently joined to a group, all its announcements must be there and viceversa
			if (toJoin != null && toJoin)
				Assert.isTrue(collectionR.containsAll(groupToJoin.getAnnouncements()));
			else if (toJoin != null && !toJoin)
				Assert.isTrue(!collectionR.containsAll(groupToJoin.getAnnouncements()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// [UC-056] Visitor create Announcement --------------------
	// v1.0 - Alicia

	@Test
	public void driverCreateAnnouncement() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> title of the Announcement.
		// testingData[i][2] -> description of the Announcement.
		// testingData[i][3] -> picture of the Announcement.
		// testingData[i][4] -> containsTaboo of the Announcement.
		// testingData[i][5] -> creationMoment of the Announcement.
		// testingData[i][6] -> group of the Announcement.
		// testingData[i][7] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Visitor correctly creates a new Announcement.
				"visitor1", "testTitle", "testDescription", "http://www.google.es", null, null, "group1", null
			}, {
				// 2 - (-) A Visitor creates an Announcement for another's visitor group.
				"visitor2", "testTitle", "testDescription", null, null, null, "group1", IllegalArgumentException.class
			}, {
				// 3 - (+) A Visitor creates an Announcement trying to edit containsTaboo and creationMoment
				"visitor1", "testTitle", "sexDescription", null, false, "future", "group1", null
			}, {
				// 4 - (-) A Visitor creates an Announcement for a null group.
				"visitor1", "testTitle", "testDescription", "http://www.google.es", null, null, null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Director creates an Announcement.
				"director1", "testTitle", "testDescription", "http://www.google.es", null, null, "group1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Group group = null;

			if (testingData[i][6] != null)
				group = this.groupService.findOne(super.getEntityId((String) testingData[i][6]));

			Date creationMoment = null;

			if (testingData[i][5] != null)
				creationMoment = new LocalDate().plusDays(5).toDate();

			this.startTransaction();

			this.templateCreateAnnouncement((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], creationMoment, group, (Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateCreateAnnouncement(final String username, final String title, final String description, final String picture, final Boolean containsTaboo, final Date creationMoment, final Group group, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Create an Announcement

			final Collection<Announcement> oldAnnouncements = this.announcementService.getByGroup(group);

			final Announcement createdAnnouncement = this.announcementService.create(group);

			if (containsTaboo != null)
				createdAnnouncement.setContainsTaboo(containsTaboo);

			if (creationMoment != null)
				createdAnnouncement.setCreationMoment(creationMoment);

			createdAnnouncement.setTitle(title);
			createdAnnouncement.setDescription(description);
			createdAnnouncement.setPicture(picture);

			final Announcement savedAnnouncement = this.announcementService.saveCreate(createdAnnouncement);

			// Flush
			this.announcementService.flush();

			// 3. Check it has been correctly created

			if (containsTaboo != null)
				Assert.isTrue(savedAnnouncement.isContainsTaboo() != containsTaboo);

			if (creationMoment != null)
				Assert.isTrue(!savedAnnouncement.getCreationMoment().equals(creationMoment));

			final Collection<Announcement> newAnnouncements = this.announcementService.getByGroup(group);
			Assert.isTrue(!oldAnnouncements.contains(savedAnnouncement));
			Assert.isTrue(newAnnouncements.contains(savedAnnouncement));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
