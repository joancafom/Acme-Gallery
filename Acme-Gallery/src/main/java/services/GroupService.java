
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GroupRepository;
import security.LoginService;
import domain.Administrator;
import domain.Announcement;
import domain.Comment;
import domain.Group;
import domain.Invitation;
import domain.Museum;
import domain.Visitor;

@Service
@Transactional
public class GroupService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private GroupRepository			groupRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	adminService;

	@Autowired
	private AnnouncementService		announcementService;

	@Autowired
	private CommentService			commentService;

	@Autowired
	private VisitorService			visitorService;

	@Autowired
	private InvitationService		invitationService;

	@Autowired
	private MuseumService			museumService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - JA
	public Group findOne(final int groupId) {
		return this.groupRepository.findOne(groupId);
	}

	// v1.0 - JA
	public Group save(final Group group) {
		Assert.notNull(group);

		//Beware to change this method! AnnouncementService.delete uses it!
		//Beware to change this method! SystemConfigurationService.updateTaboo uses it!
		return this.groupRepository.save(group);
	}

	// v1.0 - Alicia
	public void delete(final Group group) {

		Assert.notNull(group);
		Assert.isTrue(this.groupRepository.exists(group.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		for (final Announcement a : group.getAnnouncements())
			this.announcementService.deleteGroup(a);

		this.announcementService.flush();

		for (final Comment c : group.getComments())
			this.commentService.delete(c);

		this.commentService.flush();

		group.getCreator().getCreatedGroups().remove(group);
		this.visitorService.save(group.getCreator());
		this.visitorService.flush();

		for (final Visitor v : group.getParticipants()) {
			v.getJoinedGroups().remove(group);
			this.visitorService.save(v);
			this.visitorService.flush();
		}

		for (final Invitation i : group.getInvitations())
			this.invitationService.delete(i);

		this.invitationService.flush();

		group.getMuseum().getGroups().remove(group);
		this.museumService.save(group.getMuseum());
		this.museumService.flush();

		this.groupRepository.delete(group);
		this.flush();
	}

	// v1.0 - Alicia
	public void flush() {
		this.groupRepository.flush();
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - JA
	public void deleteOwner(final Group group) {

		Assert.notNull(group);
		Assert.isTrue(this.groupRepository.exists(group.getId()));

		//Make sure the owner is the Actor who is trying to perform the operation
		//Or the owner

		final Visitor owner = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(owner);
		Assert.isTrue(owner.getCreatedGroups().contains(group));

		//She or he may only remove it if he is the only member in that group and meeting date is in future

		final Date now = new Date();

		Assert.notNull(group.getParticipants());
		Assert.notNull(group.getMeetingDate());
		Assert.isTrue(group.getParticipants().size() == 1);
		Assert.isTrue(group.getParticipants().contains(owner));
		Assert.isTrue(now.before(group.getMeetingDate()));

		for (final Announcement a : group.getAnnouncements())
			this.announcementService.deleteGroup(a);

		this.announcementService.flush();

		for (final Comment c : group.getComments())
			this.commentService.delete(c);

		this.commentService.flush();

		for (final Invitation i : group.getInvitations())
			this.invitationService.delete(i);

		owner.getCreatedGroups().remove(group);
		this.visitorService.save(owner);
		this.visitorService.flush();

		for (final Visitor v : group.getParticipants()) {
			v.getJoinedGroups().remove(group);
			this.visitorService.save(v);
			this.visitorService.flush();
		}

		this.invitationService.flush();

		group.getMuseum().getGroups().remove(group);
		this.museumService.save(group.getMuseum());
		this.museumService.flush();

		this.groupRepository.delete(group);
		this.flush();
	}

	public Collection<Group> findTabooed() {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.groupRepository.findTabooed();
	}

	public Page<Group> findTabooed(final int page, final int size) {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.groupRepository.findTabooed(new PageRequest(page - 1, size));
	}

	public Collection<Group> findNotTabooed() {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.groupRepository.findNotTabooed();
	}

	public Page<Group> findNotTabooed(final int page, final int size) {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.groupRepository.findNotTabooed(new PageRequest(page - 1, size));
	}

	public Page<Group> findAll(final Integer page, final int size) {
		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.groupRepository.findAll(new PageRequest(page - 1, size));
	}

	public Page<Group> findAllOpenAndMember(final Integer page, final int size) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		return this.groupRepository.findAllOpenAndMember(visitor, new PageRequest(page - 1, size));
	}

	//v1.0 - Implemented by JA
	public void joinPublicGroup(final Group group) {

		Assert.notNull(group);

		//Retrieve the group from the BD to ensure valid Data
		final Group retrievedGroup = this.findOne(group.getId());
		Assert.notNull(retrievedGroup);

		//Retrieve the current Visitor
		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		final Date now = new Date();

		Assert.isTrue(!retrievedGroup.getIsClosed());
		Assert.isTrue(now.before(retrievedGroup.getMeetingDate()));
		Assert.isTrue(retrievedGroup.getMaxParticipants() > retrievedGroup.getParticipants().size());
		Assert.isTrue(!retrievedGroup.getParticipants().contains(currentVisitor));

		currentVisitor.getJoinedGroups().add(retrievedGroup);
		this.visitorService.save(currentVisitor);

		group.getParticipants().add(currentVisitor);
		this.save(retrievedGroup);
	}

	//v1.0 - Implemented by JA
	public void quitGroup(final Group group) {

		Assert.notNull(group);

		//Retrieve the group from the BD to ensure valid Data
		final Group retrievedGroup = this.findOne(group.getId());
		Assert.notNull(retrievedGroup);

		//Retrieve the current Visitor
		final Visitor currentVisitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentVisitor);

		final Date now = new Date();

		Assert.isTrue(now.before(retrievedGroup.getMeetingDate()));
		Assert.isTrue(retrievedGroup.getParticipants().contains(currentVisitor));
		Assert.isTrue(!retrievedGroup.getCreator().equals(currentVisitor));

		currentVisitor.getJoinedGroups().remove(retrievedGroup);
		this.visitorService.save(currentVisitor);

		group.getParticipants().remove(currentVisitor);
		this.save(retrievedGroup);
	}

	/* v1.0 - josembell */
	public Page<Group> findByMuseum(final Museum museum, final Integer page, final int size) {
		return this.groupRepository.findByMuseum(museum.getId(), new PageRequest(page - 1, size));
	}

}
