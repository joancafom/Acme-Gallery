
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
	 * 1. Log in as administrator
	 * 2. List the taboo announcements
	 * 3. Delete an announcement
	 * 
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
	// Related requirements:
	//   · REQ 24.13: An actor who is authenticated as a visitor must be able to:
	//				  List a stream with the announcements of the groups which she or he is a member of
	//				  (including the ones she or he has created) ordered by date of creation of the
	//		  		  announcement.
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
}
