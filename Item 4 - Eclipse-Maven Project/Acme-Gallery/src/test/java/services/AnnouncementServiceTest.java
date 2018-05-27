
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
import domain.Announcement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private AnnouncementService	announcementService;

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
}
