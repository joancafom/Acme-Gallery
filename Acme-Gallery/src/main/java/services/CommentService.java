
package services;

import java.util.Collection;

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


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Comment save(final Comment comment) {

		//Beware to modify this method! It is used by SystemConfigurationService.updateTaboo

		Assert.notNull(comment);
		return this.commentRepository.save(comment);
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
