
package services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import security.UserAccount;
import domain.Director;

@Service
@Transactional
public class DirectorService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	// Supporting Services ----------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

	public Director findByUserAccount(final UserAccount userAccount) {

		//Safety mesurements in supermethod

		return super.findByUserAccount(userAccount, Director.class);
	}

}
