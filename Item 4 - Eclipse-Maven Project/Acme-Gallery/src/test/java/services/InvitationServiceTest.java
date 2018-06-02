
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Group;
import domain.Invitation;
import domain.Visitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class InvitationServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private InvitationService	invitationService;

	//Fixtures ---------------------------------------

	@Autowired
	private VisitorService		visitorService;

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private GroupService		groupService;


	/*
	 * [UC-048] - Create an Invitation
	 * v1.0 - josembell
	 */
	@Test
	public void driverCreateInvitation() {
		final Object testingData[][] = {
			{
				/* + 1. Un visitor crea una invitacion para un visitor a un grupo suyo */
				"visitor2", "visitor7", "group2", "This is a test", null
			}, {
				/* - 2. Un usuario no identificado crea una invitacion */
				null, "visitor4", "group2", "This is a test", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es visitor crea una invicatión */
				"director1", "visitor7", "group2", "This is a test", IllegalArgumentException.class
			}, {
				/* - 4. Un visitor crea una invitación para un grupo que no es suyo */
				"visitor2", "visitor1", "group9", "This is a test", IllegalArgumentException.class
			}, {
				/* - 5. Un visitor crea una invitación para un grupo abierto */
				"visitor1", "visitor4", "group1", "This is a test", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			final Group group = this.groupService.findOne(this.getEntityId((String) testingData[i][2]));
			final Visitor visitor = this.visitorService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();
			//System.out.println(i);
			this.templateCreateInvitation((String) testingData[i][0], visitor, group, (String) testingData[i][3], (Class<?>) testingData[i][4]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	/* v1.0 - josembell */
	protected void templateCreateInvitation(final String username, final Visitor visitor, final Group group, final String text, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* create a invitation */
			final Invitation invitation = this.invitationService.create(group);
			invitation.setGuest(visitor);
			invitation.setMessage(text);

			final Invitation saved = this.invitationService.save(invitation);
			this.invitationService.flush();

			final Visitor host = this.visitorService.findByUserAccount(LoginService.getPrincipal());
			final Visitor guest = this.visitorService.findOne(visitor.getId());

			Assert.isTrue(host.getSentInvitations().contains(saved));
			Assert.isTrue(guest.getReceivedInvitations().contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-049] - Reject an Invitation
	 * v1.0 - josembell
	 */
	@Test
	public void driverRejectInvitation() {
		final Object testingData[][] = {
			{
				/* + 1. Un visitor rechaza una invitacion suya */
				"visitor1", "invitation19", null
			}, {
				/* - 2. Un usuario no identificado rechaza una invitación */
				null, "invitation19", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es visitor rechaza una invitacion */
				"director1", "invitation19", IllegalArgumentException.class
			}, {
				/* - 4. Un visitor rechaza una invitacion que no es suya */
				"visitor3", "invitation19", IllegalArgumentException.class
			}, {
				/* - 5. Un visitor rechaza una invitación que ya ha sido aceptada/rechazada */
				"visitor1", "invitation3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			final Invitation invitation = this.invitationService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();
			//System.out.println(i);
			this.templateRejectInvitation((String) testingData[i][0], invitation, (Class<?>) testingData[i][2]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	/* v1.0 - josembell */
	protected void templateRejectInvitation(final String username, final Invitation invitation, final Class<?> expected) {
		Class<?> caught = null;
		/* authenticate */
		this.authenticate(username);

		try {
			/* reject the invitation */
			this.invitationService.process(invitation, false);
			this.invitationService.flush();

			final Group group = this.groupService.findOne(invitation.getGroup().getId());
			Assert.isTrue(!group.getParticipants().contains(this.visitorService.findByUserAccount(LoginService.getPrincipal())));

			final Collection<Invitation> invitations = this.invitationService.findAllReceivedByVisitor(this.visitorService.findByUserAccount(LoginService.getPrincipal()));
			Assert.isTrue(invitations.contains(invitation));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-049] - Accept an Invitation
	 * v1.0 - josembell
	 */
	@Test
	public void driverAcceptInvitation() {
		final Object testingData[][] = {
			{
				/* + 1. Un visitor acepta una invitacion suya */
				"visitor1", "invitation19", null
			}, {
				/* - 2. Un usuario no identificado acepta una invitación */
				null, "invitation19", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es visitor acepta una invitacion */
				"director1", "invitation19", IllegalArgumentException.class
			}, {
				/* - 4. Un visitor acepta una invitacion que no es suya */
				"visitor3", "invitation19", IllegalArgumentException.class
			}, {
				/* - 5. Un visitor acepta una invitación que ya ha sido aceptada/rechazada */
				"visitor1", "invitation3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			final Invitation invitation = this.invitationService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();
			//System.out.println(i);
			this.templateAcceptInvitation((String) testingData[i][0], invitation, (Class<?>) testingData[i][2]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	/* v1.0 - josembell */
	protected void templateAcceptInvitation(final String username, final Invitation invitation, final Class<?> expected) {
		Class<?> caught = null;
		/* authenticate */
		this.authenticate(username);

		try {
			/* reject the invitation */
			this.invitationService.process(invitation, true);
			this.invitationService.flush();

			final Group group = this.groupService.findOne(invitation.getGroup().getId());
			Assert.isTrue(group.getParticipants().contains(this.visitorService.findByUserAccount(LoginService.getPrincipal())));

			final Collection<Invitation> invitations = this.invitationService.findAllReceivedByVisitor(this.visitorService.findByUserAccount(LoginService.getPrincipal()));
			Assert.isTrue(invitations.contains(invitation));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

}
