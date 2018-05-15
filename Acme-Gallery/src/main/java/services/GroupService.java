
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GroupRepository;
import security.LoginService;
import domain.Administrator;
import domain.Group;

@Service
@Transactional
public class GroupService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private GroupRepository			groupRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	adminService;


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

	//Other Business Methods --------------------------------------------------------------------------

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
}
