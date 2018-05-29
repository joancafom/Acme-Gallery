
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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
	private GroupRepository				groupRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService		adminService;

	@Autowired
	private AnnouncementService			announcementService;

	@Autowired
	private CommentService				commentService;

	@Autowired
	private VisitorService				visitorService;

	@Autowired
	private InvitationService			invitationService;

	@Autowired
	private MuseumService				museumService;

	@Autowired
	private Validator					validator;

	@Autowired
	private SystemConfigurationService	sysConfigService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	/* v1.0 - josembell */
	public Group create() {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Group group = new Group();
		group.setAnnouncements(new HashSet<Announcement>());
		group.setComments(new HashSet<Comment>());
		group.setCreator(visitor);
		group.setInvitations(new HashSet<Invitation>());
		group.setParticipants(new HashSet<Visitor>());
		group.getParticipants().add(visitor);

		return group;
	}

	// v1.0 - JA
	public Group findOne(final int groupId) {
		return this.groupRepository.findOne(groupId);
	}

	// v1.0 - JA
	public Collection<Group> findAll() {

		return this.groupRepository.findAll();
	}

	// v1.0 - JA
	// v2.0 - JA (taboo)
	public Group save(final Group group) {
		Assert.notNull(group);

		//Beware to change this method! AnnouncementService.delete uses it!
		//Beware to change this method! SystemConfigurationService.updateTaboo uses it!

		//Check for taboo
		final Boolean veredict = this.sysConfigService.containsTaboo(group.getName() + " " + group.getDescription());
		group.setContainsTaboo(veredict);

		return this.groupRepository.save(group);
	}

	/* v1.0 - josembell */
	public Group saveCreate(final Group group) {
		Assert.notNull(group);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(group.getCreator().equals(visitor));

		/* Make sure that the meeting date is in the future */
		final Date now = new Date();
		Assert.isTrue(group.getMeetingDate().after(now));

		/* Set the creation date */
		group.setCreationMoment(new Date(System.currentTimeMillis() - 1000));

		/* Add the creator to the collection of participants */
		group.getParticipants().add(visitor);

		/* Check if it contains a taboo word */
		final Boolean result = this.sysConfigService.containsTaboo(group.getName() + " " + group.getDescription());
		group.setContainsTaboo(result);

		final Group saved = this.groupRepository.save(group);

		visitor.getCreatedGroups().add(saved);
		visitor.getJoinedGroups().add(saved);
		saved.getMuseum().getGroups().add(saved);

		this.visitorService.save(visitor);
		this.museumService.save(saved.getMuseum());

		return saved;

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

	/* v1.0 - josembell */
	public Group reconstruct(final Group prunedGroup, final BindingResult binding) {
		Assert.notNull(prunedGroup);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		Group group = new Group();

		if (prunedGroup.getId() == 0) {
			group = prunedGroup;
			group.setAnnouncements(new HashSet<Announcement>());
			group.setComments(new HashSet<Comment>());
			group.setContainsTaboo(false);
			group.setCreator(visitor);
			group.setInvitations(new HashSet<Invitation>());
			group.setParticipants(new HashSet<Visitor>());
			group.setCreationMoment(new Date(System.currentTimeMillis() - 1000));
		}

		this.validator.validate(group, binding);
		return group;
	}

	/* v1.0 - josembell */
	public Page<Group> findAllOpen(final Integer page, final int size) {
		return this.groupRepository.findAllOpen(new PageRequest(page - 1, size));
	}

	/* v1.0 - josembell */
	public Page<Group> findAllJoinedByPrincipal(final Integer page, final int size) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);
		return this.groupRepository.findAllJoinedByPrinciapl(visitor.getId(), new PageRequest(page - 1, size));
	}

	/* v1.0 - josembell */
	public Page<Group> findAllCreatedByPrincipal(final Integer page, final int size) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);
		return this.groupRepository.findAllCreatedByPrinciapl(visitor.getId(), new PageRequest(page - 1, size));
	}

}
