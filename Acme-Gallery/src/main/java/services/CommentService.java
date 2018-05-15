
package services;

import java.util.Collection;
import java.util.HashSet;

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
	private GroupService			groupService;

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

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		//Every children comment will be deleted as well
		final Collection<Comment> childrenComments = new HashSet<Comment>(comment.getChildrenComments());
		for (final Comment childComment : childrenComments)
			this.delete(childComment);

		//We remove this comment from the parent
		comment.getParentComment().getChildrenComments().remove(comment);
		this.save(comment.getParentComment());
		this.flush();

		comment.getGroup().getComments().remove(comment);
		this.groupService.save(comment.getGroup());
		this.groupService.flush();

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
}
