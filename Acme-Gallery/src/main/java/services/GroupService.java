
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GroupRepository;
import domain.Group;

@Service
@Transactional
public class GroupService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private GroupRepository	announcementRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - JA
	public Group findOne(final int groupId) {
		return this.announcementRepository.findOne(groupId);
	}

	// v1.0 - JA
	public Group save(final Group group) {
		Assert.notNull(group);

		//Beware to change this method! AnnouncementService.delete uses it!
		return this.announcementRepository.save(group);
	}

	//Other Business Methods --------------------------------------------------------------------------
}
