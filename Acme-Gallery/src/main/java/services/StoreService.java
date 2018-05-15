
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.StoreRepository;
import domain.Store;

@Service
@Transactional
public class StoreService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private StoreRepository	storeRepository;


	// Supporting Services ----------------------------------------------------------------------------

	/* v1.0 - josembell */
	public Store findOne(final int storeId) {
		return this.storeRepository.findOne(storeId);
	}

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

}
