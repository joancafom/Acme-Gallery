
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.InvitationRepository;
import security.LoginService;
import domain.Administrator;
import domain.Invitation;

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
	private GroupService			groupService;

	@Autowired
	private VisitorService			visitorService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public void delete(final Invitation invitation) {

		Assert.notNull(invitation);
		Assert.isTrue(this.invitationRepository.exists(invitation.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

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

}
