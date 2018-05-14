
package services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import security.UserAccount;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService extends ActorService {

	//Managed Repository

	//CRUD Methods

	//Other Business Methods

	public Administrator findByUserAccount(final UserAccount userAccount) {

		//Safety mesurements in supermethod

		return super.findByUserAccount(userAccount, Administrator.class);
	}

}
