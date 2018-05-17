
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MuseumRepository;
import security.LoginService;
import domain.DayPass;
import domain.Director;
import domain.Group;
import domain.Guide;
import domain.Museum;
import domain.Review;
import domain.Room;

@Service
@Transactional
public class MuseumService extends ActorService {

	//Managed Repository
	@Autowired
	private MuseumRepository	museumRepository;

	//Supporting Services
	@Autowired
	private DirectorService		directorService;

	//Validator
	@Autowired
	private Validator			validator;


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Museum create() {

		final Museum res = new Museum();

		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		res.setGuides(new HashSet<Guide>());
		res.setRooms(new HashSet<Room>());
		res.setDayPasses(new HashSet<DayPass>());
		res.setReviews(new HashSet<Review>());
		res.setGroups(new HashSet<Group>());
		res.setDirector(currentDirector);

		return res;
	}

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

	/* v1.0 - josembell */
	public Page<Museum> findAllPaginatedByDirector(final Integer page, final int size, final Director director) {
		Assert.notNull(director);
		final Page<Museum> res = this.museumRepository.findAllByDirector(director.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Museum save(final Museum museum) {
		//Beware to modify this method! It is being used by other methods!
		Assert.notNull(museum);
		return this.museumRepository.save(museum);
	}

	// v1.0 - Alicia
	public void flush() {
		this.museumRepository.flush();
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Museum reconstructEdit(final Museum prunedMuseum, final BindingResult binding) {

		Assert.notNull(prunedMuseum);

		final Museum reconstructedMuseum;

		if (prunedMuseum.getId() == 0) {
			reconstructedMuseum = this.create();
			reconstructedMuseum.setIdentifier(prunedMuseum.getIdentifier());
		} else {
			final Museum retrievedMuseum = this.findOne(prunedMuseum.getId());
			Assert.notNull(retrievedMuseum);

			reconstructedMuseum = prunedMuseum;

			reconstructedMuseum.setDayPasses(new HashSet<DayPass>(retrievedMuseum.getDayPasses()));
			reconstructedMuseum.setDirector(retrievedMuseum.getDirector());
			reconstructedMuseum.setGroups(new HashSet<Group>(retrievedMuseum.getGroups()));
			reconstructedMuseum.setGuides(new HashSet<Guide>(retrievedMuseum.getGuides()));
			reconstructedMuseum.setReviews(new HashSet<Review>(retrievedMuseum.getReviews()));
			reconstructedMuseum.setRooms(new HashSet<Room>(retrievedMuseum.getRooms()));
			reconstructedMuseum.setIdentifier(retrievedMuseum.getIdentifier());
		}

		reconstructedMuseum.setName(prunedMuseum.getName());
		reconstructedMuseum.setAddress(prunedMuseum.getAddress());
		reconstructedMuseum.setCoordinates(prunedMuseum.getCoordinates());
		reconstructedMuseum.setEmail(prunedMuseum.getEmail());
		reconstructedMuseum.setPhoneNumber(prunedMuseum.getPhoneNumber());

		if ("".equals(prunedMuseum.getSlogan()))
			reconstructedMuseum.setSlogan(null);
		else
			reconstructedMuseum.setSlogan(prunedMuseum.getSlogan());

		if ("".equals(prunedMuseum.getBanner()))
			reconstructedMuseum.setBanner(null);
		else
			reconstructedMuseum.setBanner(prunedMuseum.getBanner());

		reconstructedMuseum.setPrice(prunedMuseum.getPrice());

		this.validator.validate(reconstructedMuseum, binding);

		return reconstructedMuseum;
	}
	// v1.0 - JA
	public Museum saveCreate(final Museum museum) {

		Assert.notNull(museum);

		//Make sure an Director is the Actor who is trying to perform the operation
		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		//As we are creating, the museum must be a brand new one
		Assert.isTrue(museum.getId() == 0);

		//The museum must be assigned to the current Director
		Assert.isTrue(currentDirector.equals(museum.getDirector()));

		final Museum savedMuseum = this.save(museum);

		//Update the associated director
		currentDirector.getMuseums().add(savedMuseum);
		this.directorService.save(currentDirector);

		return savedMuseum;
	}

	// v1.0 - JA
	public Museum saveEdit(final Museum museum) {

		Assert.notNull(museum);

		//Make sure an Director is the Actor who is trying to perform the operation
		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		//As we are creating, the museum must be an existing one
		Assert.isTrue(museum.getId() != 0);
		Assert.isTrue(this.museumRepository.exists(museum.getId()));

		//The museum must be assigned to the current Director
		Assert.isTrue(currentDirector.equals(museum.getDirector()));

		final Museum savedMuseum = this.save(museum);

		return savedMuseum;
	}

}
