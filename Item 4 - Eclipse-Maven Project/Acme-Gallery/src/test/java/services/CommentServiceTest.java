
package services;

import java.util.ArrayList;
import java.util.List;

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
import domain.Comment;
import domain.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private CommentService				commentService;

	//Fixtures ---------------------------------------

	@Autowired
	private AdministratorService		adminService;

	@Autowired
	private SystemConfigurationService	sysConfigService;

	@PersistenceContext
	private EntityManager				entityManager;

	@Autowired
	private GroupService				groupService;


	// -------------------------------------------------------------------------------
	// [UC-008] Administrator delete comments.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverListTabooAndRemove() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> the comment to remove from the System.
		// testingData[i][2] -> if we want to create a new Comment that will not be persisted, for testing purporses
		// testingData[i][3] -> thrown exception.

		final Object testingData[][] = {
			{
				// + 1) An Admin correctly retrieves the lists of Taboo Comments, selects an existing one with no replies and successfully removes it.
				"admin", "comment5", false, null
			}, {
				// + 2) An Admin correctly retrieves the lists of Taboo Comments, selects an existing one with replies and successfully removes it.
				"admin", "comment6", false, null
			}, {
				// - 3) An Admin tries to remove a null comment
				"admin", null, false, IllegalArgumentException.class
			}, {
				// - 4) A Sponsor tries to remove a comment
				"sponsor1", "comment5", false, IllegalArgumentException.class
			}, {
				// - 3) An Admin tries to remove a non-existing comment
				"admin", "comment5", true, IllegalArgumentException.class
			}
		};

		Comment commentToRemove;

		for (int i = 0; i < testingData.length; i++) {
			commentToRemove = null;

			if ((String) testingData[i][1] != null)
				commentToRemove = this.commentService.findOne(super.getEntityId((String) testingData[i][1]));

			if ((Boolean) testingData[i][2]) {
				this.authenticate(commentToRemove.getVisitor().getUserAccount().getUsername());
				commentToRemove = this.commentService.create(commentToRemove);
				this.unauthenticate();

			}

			this.startTransaction();

			this.templateListTabooAndRemove((String) testingData[i][0], commentToRemove, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateListTabooAndRemove(final String username, final Comment commentToRemove, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List Taboo Comments
			final List<Comment> tabooCommentsBefore = new ArrayList<Comment>();

			//Surround with try/catch to keep checking the testCase even if a non-admin tries to retrieve the comments
			try {
				tabooCommentsBefore.addAll(this.commentService.findTabooed());
			} catch (final Throwable oops) {
				if (this.adminService.findByUserAccount(LoginService.getPrincipal()) != null)
					throw new RuntimeException("An admin could not retrieve taboo comments!");
			}

			//To be Taboo Comments, they all must contain some taboo words
			for (final Comment c : tabooCommentsBefore)
				if (!this.sysConfigService.containsTaboo(c.getTitle() + " " + c.getDescription()))
					throw new RuntimeException("Taboo comments must contain Taboo words!");

			// 3. Delete a comment

			this.commentService.delete(commentToRemove);

			// Flush
			this.commentService.flush();

			final List<Comment> tabooCommentsAfter = new ArrayList<Comment>(this.commentService.findTabooed());
			Assert.isTrue(tabooCommentsBefore.size() == tabooCommentsAfter.size() + 1);
			Assert.isTrue(!tabooCommentsAfter.contains(commentToRemove));
			if (commentToRemove.isContainsTaboo())
				Assert.isTrue(tabooCommentsBefore.contains(commentToRemove));
			Assert.isNull(this.commentService.findOne(commentToRemove.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * [UC-042] - Create a Comment
	 * v1.0 - josembell
	 */
	@Test
	public void driverCreateComment() {
		final Object testingData[][] = {
			{
				/* + 1. Un visitor crea un comentario en un grupo al que pertenece */
				"visitor1", "group1", "Test", "This is a test", "http://www.google.es", null
			}, {
				/* - 2. Un usuario no identificado crea un comentario en un grupo */
				null, "group1", "Test", "This is a test", "http://www.google.es", IllegalArgumentException.class
			}, {
				/* - 3. Un visitor crea un comentario en un grupo al que no pertenece */
				"visitor1", "group7", "Test", "This is a test", "http://www.google.es", IllegalArgumentException.class
			}, {
				/* - 4. Un usuario que no es un visitor crea un comentario */
				"director1", "group1", "Test", "This is a test", "http://www.google.es", IllegalArgumentException.class
			}, {
				/* - 5. Un visitor crea un comentario vacio */
				"director1", "group1", null, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			final Group group = this.groupService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();
			//System.out.println(i);
			this.templateCreateComment((String) testingData[i][0], group, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	/* v1.0 - josembell */
	protected void templateCreateComment(final String username, final Group group, final String title, final String description, final String picture, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 2. create the comment */
			final Comment comment = this.commentService.create(group);
			comment.setTitle(title);
			comment.setDescription(description);
			comment.setPicture(picture);

			final Comment saved = this.commentService.saveCreate(comment);
			/* > flush */
			this.commentService.flush();

			final Group savedGroup = this.groupService.findOne(group.getId());
			Assert.isTrue(savedGroup.getComments().contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
