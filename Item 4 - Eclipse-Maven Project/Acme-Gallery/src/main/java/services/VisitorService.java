
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
import org.springframework.validation.Validator;

import repositories.VisitorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Comment;
import domain.DayPass;
import domain.Group;
import domain.Invitation;
import domain.Review;
import domain.Visitor;
import forms.ActorEditionForm;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class VisitorService extends ActorService {

	//Managed Repository
	@Autowired
	private VisitorRepository		visitorRepositories;

	// Validator

	@Autowired
	private Validator				validator;

	@Autowired
	private AdministratorService	administratorService;


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

	// v1.0 - Alicia
	public void flush() {
		this.visitorRepositories.flush();
	}

	/* v1.0 - josembell */
	public Visitor findOne(final int visitorId) {
		return this.visitorRepositories.findOne(visitorId);
	}

	// v1.0 - JA
	public Collection<Visitor> findAll() {
		return this.visitorRepositories.findAll();
	}

	//Other Business Methods

	//v1.0 - JA
	public Boolean canBeSentInvitation(final Visitor visitor, final Group group) {

		Assert.notNull(visitor);
		Assert.notNull(group);

		return this.visitorRepositories.fulfillReqsNotToBeSent(visitor.getId(), group) == null ? true : false;
	}

	//v1.0 - Implemented by JA
	public Visitor reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {

		final Visitor res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	// v1.0 - Alicia
	public Visitor reconstructEditForm(final ActorEditionForm actorEditionForm, final BindingResult binding) {
		Assert.notNull(actorEditionForm);

		final Visitor res = this.create();

		final Visitor oldVisitor = this.findByUserAccount(LoginService.getPrincipal());

		res.setId(oldVisitor.getId());
		res.setVersion(oldVisitor.getVersion());

		res.setName(actorEditionForm.getName());
		res.setSurnames(actorEditionForm.getSurnames());
		res.setEmail(actorEditionForm.getEmail());
		res.setPhoneNumber(actorEditionForm.getPhoneNumber());

		if (actorEditionForm.getAddress() == null || actorEditionForm.getAddress().equals(""))
			res.setAddress(null);
		else
			res.setAddress(actorEditionForm.getAddress());

		if (actorEditionForm.getGender() == null || actorEditionForm.getGender().equals(""))
			res.setGender(null);
		else
			res.setGender(actorEditionForm.getGender());

		res.setUserAccount(oldVisitor.getUserAccount());
		res.setCreatedGroups(oldVisitor.getCreatedGroups());
		res.setJoinedGroups(oldVisitor.getJoinedGroups());
		res.setComments(oldVisitor.getComments());
		res.setDayPasses(oldVisitor.getDayPasses());
		res.setReviews(oldVisitor.getReviews());
		res.setReceivedInvitations(oldVisitor.getReceivedInvitations());
		res.setSentInvitations(oldVisitor.getSentInvitations());

		this.validator.validate(res, binding);

		return res;
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

	// v1.0 - Alicia
	public Visitor saveEdit(final Visitor visitor) {
		Assert.notNull(visitor);

		final Visitor principal = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(principal);

		Assert.isTrue(visitor.getId() != 0);
		Assert.isTrue(visitor.getId() == principal.getId());

		Assert.notNull(visitor.getUserAccount());
		Assert.notNull(visitor.getUserAccount().getPassword());

		return this.save(visitor);
	}

	// v1.0 - Alicia
	public Visitor findByUserAccount(final UserAccount userAccount) {
		return super.findByUserAccount(userAccount, Visitor.class);
	}

	/* v1.0 - josembell */
	public Page<Visitor> findAllByGroup(final Integer page, final int size, final Group group) {
		final Page<Visitor> res = this.visitorRepositories.findAllByGroup(group.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Visitor> findAllUnlocked() {
		final Administrator administator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administator);

		final Collection<Visitor> res = this.visitorRepositories.findAllUnlocked();
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Page<Visitor> findAllUnlocked(final Integer page, final int size) {
		final Page<Visitor> res = this.visitorRepositories.findAllUnlocked(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Visitor> findAllLocked() {
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		final Collection<Visitor> res = this.visitorRepositories.findAllLocked();
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Page<Visitor> findAllLocked(final Integer page, final int size) {
		final Page<Visitor> res = this.visitorRepositories.findAllLocked(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	//v1.0 - JA
	public Collection<Visitor> findRemainingByGroup(final Group group) {

		Assert.notNull(group);

		return this.visitorRepositories.findRemainingByGroup(group);
	}

	/* v1.0 - josembell */
	// v2.0 - Alicia
	public void ban(final Visitor visitor) {
		Assert.notNull(visitor);
		Assert.isTrue(this.visitorRepositories.exists(visitor.getId()));
		Assert.isTrue(visitor.getUserAccount().getIsLocked() == false);

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		visitor.getUserAccount().setIsLocked(true);

		this.visitorRepositories.save(visitor);

	}

}
