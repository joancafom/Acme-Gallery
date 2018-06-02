
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Announcement;
import domain.Comment;
import domain.Group;
import domain.Review;

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

	@Autowired
	private GroupService				groupService;

	@Autowired
	private AnnouncementService			announcementService;

	@Autowired
	private CommentService				commentService;

	@Autowired
	private ReviewService				reviewService;


	/*
	 * [UC-015] - Add a taboo word
	 * v1.0 - josembell
	 */
	@Test
	public void driverAddTabooWord() {

		final Object testingData[][] = {
			{
				/* + 1. Un admin añade una taboo word válida */
				"admin", "test", null
			}, {
				/* - 2. Un usuario no identificado añade una taboo word */
				null, "test", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es admin añade una taboo word */
				"visitor1", "test", IllegalArgumentException.class
			}, {
				/* - 4. Un admin añade una taboo word null */
				"admin", null, IllegalArgumentException.class
			}, {
				/* - 5. Un admin añade una taboo word que ya estaba puesta */
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

			/* Groups, Announcements, Comments, Reviews before adding the taboo word */
			final Collection<Group> gBefore = this.groupService.findTabooed();
			final Collection<Announcement> aBefore = this.announcementService.findTabooed();
			final Collection<Comment> cBefore = this.commentService.findTabooed();
			final Collection<Review> rBefore = this.reviewService.findTabooed();

			final Group group = this.groupService.findOne(this.getEntityId("group1"));
			group.setName(tabooWord);
			this.groupService.save(group);
			final Announcement announcement = this.announcementService.findOne(this.getEntityId("announcement1"));
			announcement.setTitle(tabooWord);
			this.announcementService.save(announcement);
			final Comment comment = this.commentService.findOne(this.getEntityId("comment1"));
			comment.setTitle(tabooWord);
			this.commentService.save(comment);
			final Review review = this.reviewService.findOne(this.getEntityId("review1"));
			review.setBody(tabooWord);
			this.reviewService.save(review);

			Assert.isTrue(!gBefore.contains(group));
			Assert.isTrue(!aBefore.contains(announcement));
			Assert.isTrue(!cBefore.contains(comment));
			Assert.isTrue(!rBefore.contains(review));

			/* 3. add the new taboo word */
			this.SystemConfigurationService.addTabooWord(tabooWord);

			/* 4. check that the new taboo word is in the list */
			final Collection<String> tabooWordsNow = this.SystemConfigurationService.getTabooWords();
			final int numTabooWordsNow = tabooWordsNow.size();

			/* Check now the list of tabooed groups, announcements, comments and reviews */
			final Collection<Group> gAfter = this.groupService.findTabooed();
			final Collection<Announcement> aAfter = this.announcementService.findTabooed();
			final Collection<Comment> cAfter = this.commentService.findTabooed();
			final Collection<Review> rAfter = this.reviewService.findTabooed();

			final Group groupAfter = this.groupService.findOne(this.getEntityId("group1"));
			final Announcement announcementAfter = this.announcementService.findOne(this.getEntityId("announcement1"));
			final Comment commentAfter = this.commentService.findOne(this.getEntityId("comment1"));
			final Review reviewAfter = this.reviewService.findOne(this.getEntityId("review1"));

			Assert.isTrue(gAfter.contains(groupAfter));
			Assert.isTrue(aAfter.contains(announcementAfter));
			Assert.isTrue(cAfter.contains(commentAfter));
			Assert.isTrue(rAfter.contains(reviewAfter));

			Assert.isTrue(numTabooWordsBefore + 1 == numTabooWordsNow);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}

	/*
	 * [UC-016] - Delete a taboo word
	 * v1.0 - josembell
	 */
	@Test
	public void driverDeleteTabooWord() {

		final Object testingData[][] = {
			{
				/* + 1. Un admin elimina una taboo word válida */
				"admin", "test", null
			}, {
				/* - 2. Un usuario no identificado elimina una taboo word */
				null, "test", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es admin elimina una taboo word */
				"visitor1", "test", IllegalArgumentException.class
			}, {
				/* - 4. Un admin elimina una taboo word null */
				"admin", null, IllegalArgumentException.class
			}, {
				/* - 5. Un admin elimina una taboo word que no existe */
				"admin", "overwatch", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			//System.out.println(i);
			this.templateDeleteTabooWord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* 1.0 - josembell */
	protected void templateDeleteTabooWord(final String username, final String tabooWord, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {

			/* 2. display the system configuration -> list of taboo words */
			final Collection<String> tabooWordsBefore = this.SystemConfigurationService.getTabooWords();
			final int numTabooWordsBefore = tabooWordsBefore.size();
			if (tabooWord != null)
				if (tabooWord.equals("test")) {
					/* Groups, Announcements, Comments, Reviews before adding the taboo word */
					final Collection<Group> gBefore = this.groupService.findTabooed();
					final Collection<Announcement> aBefore = this.announcementService.findTabooed();
					final Collection<Comment> cBefore = this.commentService.findTabooed();
					final Collection<Review> rBefore = this.reviewService.findTabooed();

					final Group group = this.groupService.findOne(this.getEntityId("group1"));
					group.setName(tabooWord);
					this.groupService.save(group);
					final Announcement announcement = this.announcementService.findOne(this.getEntityId("announcement1"));
					announcement.setTitle(tabooWord);
					this.announcementService.save(announcement);
					final Comment comment = this.commentService.findOne(this.getEntityId("comment1"));
					comment.setTitle(tabooWord);
					this.commentService.save(comment);
					final Review review = this.reviewService.findOne(this.getEntityId("review1"));
					review.setBody(tabooWord);
					this.reviewService.save(review);

					Assert.isTrue(!gBefore.contains(group));
					Assert.isTrue(!aBefore.contains(announcement));
					Assert.isTrue(!cBefore.contains(comment));
					Assert.isTrue(!rBefore.contains(review));

					this.SystemConfigurationService.addTabooWord(tabooWord);

					final Collection<String> tabooWordsNow = this.SystemConfigurationService.getTabooWords();
					final int numTabooWordsNow = tabooWordsNow.size();

					final Collection<Group> gAfter = this.groupService.findTabooed();
					final Collection<Announcement> aAfter = this.announcementService.findTabooed();
					final Collection<Comment> cAfter = this.commentService.findTabooed();
					final Collection<Review> rAfter = this.reviewService.findTabooed();

					final Group groupAfter = this.groupService.findOne(this.getEntityId("group1"));
					final Announcement announcementAfter = this.announcementService.findOne(this.getEntityId("announcement1"));
					final Comment commentAfter = this.commentService.findOne(this.getEntityId("comment1"));
					final Review reviewAfter = this.reviewService.findOne(this.getEntityId("review1"));

					Assert.isTrue(gAfter.contains(groupAfter));
					Assert.isTrue(aAfter.contains(announcementAfter));
					Assert.isTrue(cAfter.contains(commentAfter));
					Assert.isTrue(rAfter.contains(reviewAfter));

					Assert.isTrue(numTabooWordsBefore + 1 == numTabooWordsNow);
				}
			/* Remove the taboo word */
			this.SystemConfigurationService.deleteTabooWord(tabooWord);

			final Collection<String> tabooWordsNow2 = this.SystemConfigurationService.getTabooWords();
			final int numTabooWordsNow2 = tabooWordsNow2.size();

			final Collection<Group> gAfter2 = this.groupService.findTabooed();
			final Collection<Announcement> aAfter2 = this.announcementService.findTabooed();
			final Collection<Comment> cAfter2 = this.commentService.findTabooed();
			final Collection<Review> rAfter2 = this.reviewService.findTabooed();

			final Group groupAfter2 = this.groupService.findOne(this.getEntityId("group1"));
			final Announcement announcementAfter2 = this.announcementService.findOne(this.getEntityId("announcement1"));
			final Comment commentAfter2 = this.commentService.findOne(this.getEntityId("comment1"));
			final Review reviewAfter2 = this.reviewService.findOne(this.getEntityId("review1"));

			Assert.isTrue(!gAfter2.contains(groupAfter2));
			Assert.isTrue(!aAfter2.contains(announcementAfter2));
			Assert.isTrue(!cAfter2.contains(commentAfter2));
			Assert.isTrue(!rAfter2.contains(reviewAfter2));

			Assert.isTrue(numTabooWordsBefore == numTabooWordsNow2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}

	/*
	 * [UC-017] - Edit VAT value
	 * v1.0 - josembell
	 */
	@Test
	public void driverEditVATValue() {

		final Object testingData[][] = {
			{
				/* + 1. Un admin edita el VAT value */
				"admin", 0.10, null
			}, {
				/* - 2. Un usuario no identificado edita el VAT value */
				null, 0.10, IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es admin edita el VAT value */
				"visitor1", 0.10, IllegalArgumentException.class
			}, {
				/* - 4. Un admin edita el VAT value sin indicar el valor */
				"admin", null, ConstraintViolationException.class
			}, {
				/* - 5. Un admin edita el VAT con un tamaño mayor a 1.0 */
				"admin", 1.01, ConstraintViolationException.class
			}, {
				/* - 6. Un admin edita el VAT con un tamaño menor que 0.0 */
				"director1", -0.01, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			//System.out.println(i);
			this.templateEditVATValue((String) testingData[i][0], (Double) testingData[i][1], (Class<?>) testingData[i][2]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* 1.0 - josembell */
	protected void templateEditVATValue(final String username, final Double vat, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 2. edit the vat value */
			this.SystemConfigurationService.getCurrentSystemConfiguration().setVAT(vat);
			this.SystemConfigurationService.save(this.SystemConfigurationService.getCurrentSystemConfiguration());

			Assert.isTrue(this.SystemConfigurationService.getCurrentSystemConfiguration().getVAT().equals(vat));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}
}
