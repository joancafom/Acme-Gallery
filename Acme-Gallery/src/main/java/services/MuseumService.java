
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.MuseumRepository;
import domain.Museum;

@Service
@Transactional
public class MuseumService extends ActorService {

	//Managed Repository
	@Autowired
	private MuseumRepository	museumRepository;


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Museum findOne(final int museumId) {

		return this.museumRepository.findOne(museumId);
	}

	//Other Business Methods

}
