
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MuseumRepository;
import domain.Museum;

@Service
@Transactional
public class MuseumService extends ActorService {

	//Managed Repository
	@Autowired
	private MuseumRepository	museumRepository;


	//CRUD Methods

	// v1.0 - Alicia
	public Collection<Museum> findAll() {
		return this.museumRepository.findAll();
	}

	// v1.0 - Alicia
	public Page<Museum> findAllPaginated(final int page, final int size) {
		final Page<Museum> res = this.museumRepository.findAll(new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by JA
	public Museum findOne(final int museumId) {

		return this.museumRepository.findOne(museumId);
	}

	//Other Business Methods

}
