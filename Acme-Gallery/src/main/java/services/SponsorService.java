
package services;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.SponsorRepository;
import security.LoginService;
import domain.Sponsor;
import domain.Sponsorship;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class SponsorService extends ActorService {

	//Managed Repository
	@Autowired
	private SponsorRepository	sponsorRepository;


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Sponsor create() {

		//Create a Sponsor by using the supertype method
		final Sponsor res = super.create(Sponsor.class);

		//Add Specific properties regarding the Sponsor
		res.setSponsorships(new HashSet<Sponsorship>());

		return res;

	}

	//v1.0 - Implemented by JA
	public Sponsor save(final Sponsor sponsorToSave) {

		Assert.notNull(sponsorToSave);

		return this.sponsorRepository.save(sponsorToSave);
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Sponsor reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {

		final Sponsor res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	//v1.0 - Implemented by JA
	public Sponsor register(final Sponsor sponsor) {

		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getId() == 0);
		Assert.notNull(sponsor.getUserAccount());
		Assert.notNull(sponsor.getUserAccount().getPassword());

		//Assert that only an unauth Actor can register
		try {
			LoginService.getPrincipal();
			throw new RuntimeException("An authenticated Actor cannot register to the system");

		} catch (final IllegalArgumentException okFlow) {
			//Intentionally left in blank
		}

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(sponsor.getUserAccount().getPassword(), null);
		sponsor.getUserAccount().setPassword(hashedPassword);

		return this.save(sponsor);
	}

}
