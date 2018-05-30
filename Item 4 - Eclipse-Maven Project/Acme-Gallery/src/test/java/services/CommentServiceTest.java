
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


	// -------------------------------------------------------------------------------
	// [UC-008] Administrator delete comments.
	// 
	// Related requirements:
	//   · REQ 22.18: An actor who is authenticated as an administrator must be able to
	//                List the taboo comments. A comment is taboo if it contains taboo words in its title
	//				  or description.:

	//   · REQ 22.19: An actor who is authenticated as an administrator must be able to
	//                Remove any comment that she or he considers inappropriate
	//
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
}
