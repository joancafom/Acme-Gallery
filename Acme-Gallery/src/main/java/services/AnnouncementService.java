
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


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

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
