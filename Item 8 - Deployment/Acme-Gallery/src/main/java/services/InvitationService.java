
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.InvitationRepository;
import security.LoginService;
import domain.Administrator;
import domain.Group;
import domain.Invitation;
import domain.Visitor;

@Service
@Transactional
public class InvitationService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private InvitationRepository	invitationRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private VisitorService			visitorService;

	@Autowired
	private GroupService			groupService;

	// Validator --------------------------------------------------------------------------------------

	@Autowired
	private Validator				validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - JA
	public Invitation create(final Group group) {

		Assert.notNull(group);
		Assert.isTrue(group.getIsClosed());

		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		Assert.isTrue(group.getCreator().equals(currentVisitor));

		final Date now = new Date();
		Assert.isTrue(now.before(group.getMeetingDate()));

		//An invitation cannot be create if the group does not allow more participants
		Assert.isTrue(group.getMaxParticipants() > group.getParticipants().size());

		final Invitation res = new Invitation();

		res.setGroup(group);
		res.setHost(currentVisitor);
		res.setSentMoment(new Date(System.currentTimeMillis() - 1000L));

		return res;
	}

	// v1.0 - JA
	public Invitation findOne(final int invitationId) {

		return this.invitationRepository.findOne(invitationId);
	}

	// v1.0 - JA
	public Invitation save(final Invitation invitation) {

		Assert.notNull(invitation);

		return this.invitationRepository.save(invitation);
	}

	// v1.0 - Alicia
	public void delete(final Invitation invitation) {

		Assert.notNull(invitation);
		Assert.isTrue(this.invitationRepository.exists(invitation.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation or the owner
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(administrator != null || visitor != null);

		if (visitor != null) {
			final Group group = invitation.getGroup();

			Assert.isTrue(visitor.getCreatedGroups().contains(group));
			final Date now = new Date();

			Assert.notNull(group.getParticipants());
			Assert.notNull(group.getMeetingDate());
			Assert.isTrue(group.getParticipants().size() == 1);
			Assert.isTrue(group.getParticipants().contains(visitor));
			Assert.isTrue(now.before(group.getMeetingDate()));
		}

		invitation.getGuest().getReceivedInvitations().remove(invitation);
		this.visitorService.save(invitation.getGuest());
		this.visitorService.flush();

		invitation.getHost().getSentInvitations().remove(invitation);
		this.visitorService.save(invitation.getHost());
		this.visitorService.flush();

		this.invitationRepository.delete(invitation);
		this.flush();
	}

	// v1.0 - Alicia
	public void flush() {
		this.invitationRepository.flush();
	}

	//Other Business Methods --------------------------------------------------------------------------

	//v1.0 - Implemented by JA
	public void process(final Invitation invitation, final boolean isAccepted) {

		Assert.notNull(invitation);
		Assert.isTrue(this.invitationRepository.exists(invitation.getId()));

		//Assert is a pending invitation
		Assert.isNull(invitation.getIsAccepted());

		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		Assert.isTrue(currentVisitor.equals(invitation.getGuest()));

		Assert.notNull(invitation.getGroup());
		final Group groupToJoin = this.groupService.findOne(invitation.getGroup().getId());
		Assert.notNull(groupToJoin);

		Assert.isTrue(groupToJoin.getInvitations().contains(invitation));
		Assert.isTrue(groupToJoin.getCreator().equals(invitation.getHost()));
		Assert.isTrue(!currentVisitor.getJoinedGroups().contains(groupToJoin));

		if (isAccepted && groupToJoin.getParticipants().size() >= groupToJoin.getMaxParticipants())
			throw new RuntimeException("You cannot overpass the max number of participants!");

		invitation.setIsAccepted(isAccepted);
		this.save(invitation);

		if (isAccepted) {

			groupToJoin.getParticipants().add(currentVisitor);
			this.groupService.save(groupToJoin);

			currentVisitor.getJoinedGroups().add(groupToJoin);
			this.visitorService.save(currentVisitor);
		}

	}

	//v1.0 - Implemented by JA
	public Collection<Invitation> findAllReceivedByVisitor(final Visitor visitor) {

		Assert.notNull(visitor);

		return this.invitationRepository.findAllReceivedByVisitorId(visitor.getId());
	}

	//v1.0 - Implemented by JA
	public Page<Invitation> findAllReceivedByVisitor(final Visitor visitor, final int page, final int size) {

		Assert.notNull(visitor);

		return this.invitationRepository.findAllReceivedByVisitorId(visitor.getId(), new PageRequest(page - 1, size));
	}

	//v1.0 - Implemented by JA
	public Invitation reconstructCreate(final Invitation invitationToReconstruct, final BindingResult binding) {

		Assert.notNull(invitationToReconstruct);
		Assert.notNull(invitationToReconstruct.getGroup());

		final Invitation res = this.create(invitationToReconstruct.getGroup());

		if ("".equals(invitationToReconstruct.getMessage()))
			res.setMessage(null);
		else
			res.setMessage(invitationToReconstruct.getMessage());

		res.setGuest(invitationToReconstruct.getGuest());

		this.validator.validate(res, binding);

		return res;
	}

	//v1.0 - Implemented by JA
	public Invitation sendInvitation(final Invitation invitation) {

		Assert.notNull(invitation);
		Assert.isTrue(invitation.getId() == 0);

		final Group group = invitation.getGroup();
		Assert.notNull(group);
		Assert.isTrue(group.getIsClosed());

		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		Assert.isTrue(currentVisitor.equals(invitation.getHost()));

		//Don't need to check if they are the same, as the owner is implicitly contained in a Group
		Assert.notNull(invitation.getGuest());
		Assert.notNull(group.getParticipants());
		Assert.isTrue(!group.getParticipants().contains(invitation.getGuest()));

		final Date now = new Date();
		Assert.isTrue(now.before(group.getMeetingDate()));

		//An invitation cannot be sent if the group does not allow more participants
		Assert.isTrue(group.getMaxParticipants() > group.getParticipants().size());

		//Make sure it does it does not belong to the group or has any pending invitation
		Assert.isTrue(this.visitorService.canBeSentInvitation(invitation.getGuest(), group));

		return this.save(invitation);
	}
}
