
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

import repositories.AnnouncementRepository;
import security.LoginService;
import domain.Administrator;
import domain.Announcement;
import domain.Director;
import domain.Group;
import domain.Visitor;

@Service
@Transactional
public class AnnouncementService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private AnnouncementRepository		announcementRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService		administratorService;

	@Autowired
	private GroupService				groupService;

	@Autowired
	private VisitorService				visitorService;

	@Autowired
	private DirectorService				directorService;

	@Autowired
	private Validator					validator;

	@Autowired
	private SystemConfigurationService	sysConfigService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	/* v1.0 - josembell */
	// v2.0 - Alicia
	public Announcement create(final Group group) {
		Assert.notNull(group);

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);
		Assert.isTrue(group.getCreator().equals(visitor));

		final Announcement announcement = new Announcement();
		announcement.setGroup(group);

		return announcement;
	}

	/* v1.0 - josembell */
	public Announcement saveCreate(final Announcement announcement) {
		Assert.notNull(announcement);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);
		Assert.isTrue(announcement.getGroup().getCreator().equals(visitor));

		announcement.setCreationMoment(new Date(System.currentTimeMillis() - 1000));

		final Boolean result = this.sysConfigService.containsTaboo(announcement.getTitle() + " " + announcement.getDescription());
		announcement.setContainsTaboo(result);

		return this.announcementRepository.save(announcement);
	}

	// v1.0 - JA
	public Announcement findOne(final int announcementId) {

		return this.announcementRepository.findOne(announcementId);
	}

	//v1.0 - Implemented by JA
	// v2.0 - JA (taboo)
	public Announcement save(final Announcement announcement) {

		Assert.notNull(announcement);

		//Beware to modify this method! It is used by SystemConfigurationService.updateTaboo

		//Check for taboo
		final Boolean veredict = this.sysConfigService.containsTaboo(announcement.getTitle() + " " + announcement.getDescription());
		announcement.setContainsTaboo(veredict);
		return this.announcementRepository.save(announcement);
	}

	// v1.0 - JA
	public void delete(final Announcement announcement) {

		Assert.notNull(announcement);
		Assert.isTrue(this.announcementRepository.exists(announcement.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		announcement.getGroup().getAnnouncements().remove(announcement);
		this.groupService.save(announcement.getGroup());

		this.announcementRepository.delete(announcement);
	}

	// v1.0 - Alicia
	// v2.0 - JA (owner)
	public void deleteGroup(final Announcement announcement) {
		Assert.notNull(announcement);
		Assert.isTrue(this.announcementRepository.exists(announcement.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation or the owner
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(administrator != null || visitor != null);

		if (visitor != null) {
			final Group group = announcement.getGroup();

			Assert.isTrue(visitor.getCreatedGroups().contains(group));
			final Date now = new Date();

			Assert.notNull(group.getParticipants());
			Assert.notNull(group.getMeetingDate());
			Assert.isTrue(group.getParticipants().size() == 1);
			Assert.isTrue(group.getParticipants().contains(visitor));
			Assert.isTrue(now.before(group.getMeetingDate()));
		}

		this.announcementRepository.delete(announcement);
		this.flush();
	}

	// v1.0 - JA
	public Collection<Announcement> findAll() {

		return this.announcementRepository.findAll();
	}

	// v1.0 - Alicia
	public void flush() {
		this.announcementRepository.flush();
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - JA
	public Page<Announcement> findAll(final int page, final int size) {

		final Page<Announcement> res = this.announcementRepository.findAll(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Collection<Announcement> findTabooed() {

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		final Collection<Announcement> res = this.announcementRepository.findTabooed();
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Announcement> findTabooed(final int page, final int size) {

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		final Page<Announcement> res = this.announcementRepository.findTabooed(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Collection<Announcement> findNotTabooed() {

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		final Collection<Announcement> res = this.announcementRepository.findNotTabooed();
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Announcement> findNotTabooed(final int page, final int size) {

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		final Page<Announcement> res = this.announcementRepository.findNotTabooed(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	// v2.0 - JA
	public Page<Announcement> findAllByGroup(final Integer page, final int size, final Group group) {

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());

		Assert.isTrue(administrator != null || visitor != null || director != null);

		final Page<Announcement> res = this.announcementRepository.findAllByGroup(group.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Announcement reconstruct(final Announcement prunedAnnouncement, final BindingResult binding) {

		Assert.notNull(prunedAnnouncement);

		Announcement announcement = new Announcement();

		if (prunedAnnouncement.getId() == 0) {
			announcement = prunedAnnouncement;
			announcement.setCreationMoment(new Date(System.currentTimeMillis() - 1000));
			announcement.setContainsTaboo(false);
		}

		this.validator.validate(announcement, binding);

		return announcement;
	}

	// v1.0 - JA
	public Collection<Announcement> getStreamByPrincipal() {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		return this.announcementRepository.getStreamByPrincipal(visitor.getId());
	}

	/* v1.0 - josembell */
	public Page<Announcement> getStreamByPrincipal(final Integer page, final int size) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);
		return this.announcementRepository.getStreamByPrincipal(visitor.getId(), new PageRequest(page - 1, size));
	}

	// v1.0 - Alicia
	public Collection<Announcement> getByGroup(final Group group) {
		Assert.notNull(group);

		return this.announcementRepository.findByGroupId(group.getId());
	}

}
