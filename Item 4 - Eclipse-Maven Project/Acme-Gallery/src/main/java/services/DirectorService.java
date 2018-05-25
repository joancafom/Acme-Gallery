
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.DirectorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Director;
import domain.Museum;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class DirectorService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private DirectorRepository		directorRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Director create() {
		final Director res = super.create(Director.class);

		res.setMuseums(new HashSet<Museum>());

		return res;
	}

	// v1.0 - JA
	public Collection<Director> findAll() {

		return this.directorRepository.findAll();
	}

	// v1.0 - Alicia
	public Director save(final Director director) {
		Assert.notNull(director);

		return this.directorRepository.save(director);
	}

	/* v1.0 - josembell */
	public Director findOne(final int directorId) {
		return this.directorRepository.findOne(directorId);
	}

	//Other Business Methods --------------------------------------------------------------------------

	public Director findByUserAccount(final UserAccount userAccount) {

		//Safety mesurements in supermethod

		return super.findByUserAccount(userAccount, Director.class);
	}

	// v1.0 - Alicia
	public Director reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {
		final Director res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	// v1.0 - Alicia
	public Director register(final Director director) {
		Assert.notNull(director);

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		Assert.isTrue(director.getId() == 0);
		Assert.notNull(director.getUserAccount());
		Assert.notNull(director.getUserAccount().getPassword());

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(director.getUserAccount().getPassword(), null);
		director.getUserAccount().setPassword(hashedPassword);

		return this.save(director);
	}

	/* v1.0 - josembell */
	public Page<Director> findAllPaginated(final Integer page, final int size) {
		final Page<Director> res = this.directorRepository.findAll(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}
}
