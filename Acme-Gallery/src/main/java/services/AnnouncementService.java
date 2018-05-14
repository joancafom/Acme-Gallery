
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

	// v1.0 - JA
	public Collection<Announcement> findAll() {

		return this.announcementRepository.findAll();
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

}