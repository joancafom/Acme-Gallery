
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import security.LoginService;
import domain.Administrator;
import domain.Comment;
import domain.Group;
import domain.Visitor;

@Service
@Transactional
public class CommentService extends ActorService {

	//Managed Repository
	@Autowired
	private CommentRepository		commentRepository;

	//Supporting Services
	@Autowired
	private AdministratorService	adminService;

	@Autowired
	private VisitorService			visitorService;


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Comment save(final Comment comment) {

		//Beware to modify this method! It is used by SystemConfigurationService.updateTaboo

		Assert.notNull(comment);
		return this.commentRepository.save(comment);
	}

	// v1.0 - Alicia
	public void delete(final Comment comment) {

		Assert.notNull(comment);
		Assert.isTrue(this.commentRepository.exists(comment.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation or the owner
		final Administrator administrator = this.adminService.findByUserAccount(LoginService.getPrincipal());
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(administrator != null || visitor != null);

		if (visitor != null) {
			final Group group = comment.getGroup();

			Assert.isTrue(visitor.getCreatedGroups().contains(group));
			final Date now = new Date();

			Assert.notNull(group.getParticipants());
			Assert.notNull(group.getMeetingDate());
			Assert.isTrue(group.getParticipants().size() == 1);
			Assert.isTrue(group.getParticipants().contains(visitor));
			Assert.isTrue(now.before(group.getMeetingDate()));
		}

		if (comment.getParentComment() != null) {
			comment.getParentComment().getChildrenComments().remove(comment);
			comment.setParentComment(null);
			this.save(comment.getParentComment());
			this.flush();
		}

		for (final Comment c : comment.getChildrenComments()) {
			c.setParentComment(null);
			this.save(c);
			this.flush();
		}

		comment.getVisitor().getComments().remove(comment);
		this.visitorService.save(comment.getVisitor());
		this.visitorService.flush();

		this.commentRepository.delete(comment);
		this.flush();
	}

	// v1.0 - Alicia
	public void flush() {
		this.commentRepository.flush();
	}

	/* v1.0 - josembell */
	public Comment findOne(final Integer commentId) {
		Assert.notNull(commentId);
		return this.commentRepository.findOne(commentId);
	}

	//Other Business Methods

	public Collection<Comment> findTabooed() {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.commentRepository.findTabooed();
	}

	public Page<Comment> findTabooed(final int page, final int size) {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.commentRepository.findTabooed(new PageRequest(page - 1, size));
	}

	public Collection<Comment> findNotTabooed() {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.commentRepository.findNotTabooed();
	}

	public Page<Comment> findNotTabooed(final int page, final int size) {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.commentRepository.findNotTabooed(new PageRequest(page - 1, size));
	}

	/* v1.0 - josembell */
	public Page<Comment> findAllRootByGroup(final Integer page, final int size, final Group group) {
		final Page<Comment> res = this.commentRepository.findAllRootByGroup(group.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Page<Comment> findRepliesByComment(final Integer page, final int size, final Comment parentComment) {
		final Page<Comment> res = this.commentRepository.findRepliesByComment(parentComment.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

}
