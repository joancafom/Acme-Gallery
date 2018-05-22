
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
	private AnnouncementRepository	announcementRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private GroupService			groupService;

	@Autowired
	private VisitorService			visitorService;

	@Autowired
	private DirectorService			directorService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - JA
	public Announcement findOne(final int announcementId) {

		return this.announcementRepository.findOne(announcementId);
	}

	//v1.0 - Implemented by JA
	public Announcement save(final Announcement announcement) {

		//Beware to modify this method! It is used by SystemConfigurationService.updateTaboo

		Assert.notNull(announcement);
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
}
