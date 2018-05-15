
package services;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.ReviewerRepository;
import security.LoginService;
import domain.Administrator;
import domain.Critique;
import domain.Reviewer;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class ReviewerService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ReviewerRepository		reviewerRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Reviewer create() {
		final Reviewer res = super.create(Reviewer.class);

		res.setCritiques(new HashSet<Critique>());

		return res;
	}

	// v1.0 - Alicia
	public Reviewer save(final Reviewer reviewer) {
		Assert.notNull(reviewer);

		return this.reviewerRepository.save(reviewer);
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - Alicia
	public Reviewer reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {
		final Reviewer res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	// v1.0 - Alicia
	public Reviewer register(final Reviewer reviewer) {
		Assert.notNull(reviewer);

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		Assert.isTrue(reviewer.getId() == 0);
		Assert.notNull(reviewer.getUserAccount());
		Assert.notNull(reviewer.getUserAccount().getPassword());

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(reviewer.getUserAccount().getPassword(), null);
		reviewer.getUserAccount().setPassword(hashedPassword);

		return this.save(reviewer);
	}
}
