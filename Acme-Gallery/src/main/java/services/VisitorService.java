
package services;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.VisitorRepository;
import security.LoginService;
import domain.Comment;
import domain.DayPass;
import domain.Group;
import domain.Invitation;
import domain.Review;
import domain.Visitor;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class VisitorService extends ActorService {

	//Managed Repository
	@Autowired
	private VisitorRepository	visitorRepositories;


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Visitor create() {

		//Create a Visitor by using the supertype method
		final Visitor res = super.create(Visitor.class);

		//Add Specific properties regarding the Visitor
		res.setComments(new HashSet<Comment>());
		res.setCreatedGroups(new HashSet<Group>());
		res.setJoinedGroups(new HashSet<Group>());
		res.setSentInvitations(new HashSet<Invitation>());
		res.setReceivedInvitations(new HashSet<Invitation>());
		res.setReviews(new HashSet<Review>());
		res.setDayPasses(new HashSet<DayPass>());

		return res;

	}

	//v1.0 - Implemented by JA
	public Visitor save(final Visitor visitorToSave) {

		Assert.notNull(visitorToSave);

		return this.visitorRepositories.save(visitorToSave);
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Visitor reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {

		final Visitor res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	//v1.0 - Implemented by JA
	public Visitor register(final Visitor visitor) {

		Assert.notNull(visitor);
		Assert.isTrue(visitor.getId() == 0);
		Assert.notNull(visitor.getUserAccount());
		Assert.notNull(visitor.getUserAccount().getPassword());

		//Assert that only an unauth Actor can register
		try {
			LoginService.getPrincipal();
			throw new RuntimeException("An authenticated Actor cannot register to the system");

		} catch (final IllegalArgumentException okFlow) {
			//Intentionally left in blank
		}

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(visitor.getUserAccount().getPassword(), null);
		visitor.getUserAccount().setPassword(hashedPassword);

		return this.save(visitor);
	}

}
