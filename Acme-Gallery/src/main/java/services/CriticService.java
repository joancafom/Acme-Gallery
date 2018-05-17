
package services;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.CriticRepository;
import security.LoginService;
import domain.Administrator;
import domain.Critic;
import domain.Critique;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class CriticService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private CriticRepository		criticRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Critic create() {
		final Critic res = super.create(Critic.class);

		res.setCritiques(new HashSet<Critique>());

		return res;
	}

	// v1.0 - Alicia
	public Critic save(final Critic critic) {
		Assert.notNull(critic);

		return this.criticRepository.save(critic);
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - Alicia
	public Critic reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {
		final Critic res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	// v1.0 - Alicia
	public Critic register(final Critic critic) {
		Assert.notNull(critic);

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		Assert.isTrue(critic.getId() == 0);
		Assert.notNull(critic.getUserAccount());
		Assert.notNull(critic.getUserAccount().getPassword());

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(critic.getUserAccount().getPassword(), null);
		critic.getUserAccount().setPassword(hashedPassword);

		return this.save(critic);
	}
}
