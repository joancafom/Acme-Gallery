
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repositories.ActorRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class ActorService {

	//Managed Repository
	@Autowired
	private ActorRepository		actorRepository;

	//Other supporting services

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	//CRUD Methods

	//v1.0 - Implemented by JA
	protected <T extends Actor> T create(final Class<T> clase) {

		// Create the Actor

		Assert.notNull(clase);

		T res = null;

		try {
			res = clase.newInstance();
		} catch (final Throwable oops) {
			throw new RuntimeException("Could not instantiate the provided class");
		}

		//Create and Associate a UserAccount
		final Authority userAuthority = new Authority();
		final UserAccount userAccount = this.userAccountService.create();

		switch (clase.getName().toUpperCase()) {

		case "DOMAIN.ADMINISTRATOR":
			userAuthority.setAuthority(Authority.ADMINISTRATOR);
			break;

		case "DOMAIN.GUIDE":
			userAuthority.setAuthority(Authority.GUIDE);
			break;

		case "DOMAIN.DIRECTOR":
			userAuthority.setAuthority(Authority.DIRECTOR);
			break;

		case "DOMAIN.VISITOR":
			userAuthority.setAuthority(Authority.VISITOR);
			break;

		case "DOMAIN.SPONSOR":
			userAuthority.setAuthority(Authority.SPONSOR);
			break;

		case "DOMAIN.CRITIC":
			userAuthority.setAuthority(Authority.CRITIC);
			break;

		default:
			throw new IllegalArgumentException("The role you are trying to create is not implemented");

		}

		userAccount.getAuthorities().add(userAuthority);
		res.setUserAccount(userAccount);

		return res;
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	//v2.0 - Updated by JA
	protected <T extends Actor> T findByUserAccount(final UserAccount userAccount, final Class<T> clase) {

		Assert.notNull(userAccount);

		final Actor res = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(res);

		if (res == null || !clase.isInstance(res))
			return null;
		else
			return clase.cast(res);
	}

	//v1.0 - Implemented by JA
	protected <T extends Actor> T reconstructRegisterForm(final T actorToReconstruct, final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {

		Assert.notNull(actorToReconstruct);

		//Assert Accepted Terms and Conditions + Password Confirmation
		Assert.isTrue(actorRegistrationForm.getAcceptedTerms());
		Assert.notNull(actorRegistrationForm.getPassword());
		Assert.notNull(actorRegistrationForm.getPasswordConfirmation());
		Assert.isTrue(actorRegistrationForm.getPassword().equals(actorRegistrationForm.getPasswordConfirmation()));

		//Actor properties
		actorToReconstruct.setName(actorRegistrationForm.getName());
		actorToReconstruct.setSurnames(actorRegistrationForm.getSurnames());
		actorToReconstruct.setEmail(actorRegistrationForm.getEmail());
		if (actorRegistrationForm.getPhoneNumber() != null)
			actorToReconstruct.setPhoneNumber(actorRegistrationForm.getPhoneNumber().replaceAll(" ", ""));
		else
			actorToReconstruct.setPhoneNumber(actorRegistrationForm.getPhoneNumber());

		if (actorRegistrationForm.getAddress() == null || actorRegistrationForm.getAddress().equals(""))
			actorToReconstruct.setAddress(null);
		else
			actorToReconstruct.setAddress(actorRegistrationForm.getAddress());

		if (actorRegistrationForm.getGender() == null || actorRegistrationForm.getGender().equals(""))
			actorToReconstruct.setGender(null);
		else
			actorToReconstruct.setGender(actorRegistrationForm.getGender());

		this.validator.validate(actorToReconstruct, binding);

		//UserAccount properties

		actorToReconstruct.getUserAccount().setUsername(actorRegistrationForm.getUsername());
		actorToReconstruct.getUserAccount().setPassword(actorRegistrationForm.getPassword());

		final Errors userAccountErrors = new BeanPropertyBindingResult(actorToReconstruct.getUserAccount(), binding.getObjectName());

		this.validator.validate(actorToReconstruct.getUserAccount(), userAccountErrors);

		binding.addAllErrors(userAccountErrors);

		return actorToReconstruct;

	}
}
