
package services;

import java.util.Collection;

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
	public void deleteGroup(final Announcement announcement) {
		Assert.notNull(announcement);
		Assert.isTrue(this.announcementRepository.exists(announcement.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

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

}
