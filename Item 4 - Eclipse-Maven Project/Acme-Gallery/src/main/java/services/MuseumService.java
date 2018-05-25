
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
import domain.GPSCoordinates;
import domain.Group;
import domain.Guide;
import domain.Museum;
import domain.Review;
import domain.Room;
import forms.MuseumForm;

@Service
@Transactional
public class MuseumService extends ActorService {

	//Managed Repository
	@Autowired
	private MuseumRepository	museumRepository;

	//Supporting Services
	@Autowired
	private DirectorService		directorService;

	@Autowired
	private GuideService		guideService;

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
	// v2.0 - JA
	public Museum save(final Museum museum) {
		//Beware to modify this method! It is being used by other methods!
		Assert.notNull(museum);

		//Museums can never have null Coordinates!

		Assert.notNull(museum.getCoordinates());
		Assert.notNull(museum.getCoordinates().getLatitude());
		Assert.notNull(museum.getCoordinates().getLongitude());

		return this.museumRepository.save(museum);
	}
	// v1.0 - Alicia
	public void flush() {
		this.museumRepository.flush();
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Collection<Museum> findWorkplacesByGuide(final Guide guide) {

		Assert.notNull(guide);

		return this.museumRepository.findWorkplacesByGuide(guide);
	}

	//v1.0 - Implemented by JA
	public Page<Museum> findWorkplacesByGuide(final Guide guide, final Integer page, final int size) {

		Assert.notNull(guide);

		return this.museumRepository.findWorkplacesByGuide(guide, new PageRequest(page - 1, size));
	}

	// v1.0 - JA
	public Museum reconstructAddGuide(final MuseumForm museumForm, final BindingResult binding) {

		Assert.notNull(museumForm);
		Assert.notNull(museumForm.getMuseum());

		final Museum res = this.create();

		final Museum oldMuseum = this.findOne(museumForm.getMuseum().getId());

		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);
		Assert.isTrue(oldMuseum.getDirector().equals(currentDirector));

		res.setId(oldMuseum.getId());
		res.setVersion(oldMuseum.getVersion());

		res.setName(oldMuseum.getName());
		res.setAddress(oldMuseum.getAddress());
		res.setCoordinates(oldMuseum.getCoordinates());
		res.setEmail(oldMuseum.getEmail());
		res.setPhoneNumber(oldMuseum.getPhoneNumber());
		res.setSlogan(oldMuseum.getSlogan());
		res.setIdentifier(oldMuseum.getIdentifier());
		res.setBanner(oldMuseum.getBanner());
		res.setPrice(oldMuseum.getPrice());
		res.setRooms(oldMuseum.getRooms());
		res.setDayPasses(oldMuseum.getDayPasses());
		res.setReviews(oldMuseum.getReviews());
		res.setDirector(oldMuseum.getDirector());
		res.setStore(oldMuseum.getStore());

		final Collection<Guide> newGuides = new HashSet<Guide>();
		newGuides.addAll(oldMuseum.getGuides());

		if (museumForm.getGuides() != null)
			for (final String s : museumForm.getGuides()) {
				final Guide g = this.guideService.findOne(new Integer(s));
				Assert.notNull(g);
				newGuides.add(g);
			}

		res.setGuides(newGuides);

		this.validator.validate(res, binding);

		return res;
	}

	//v1.0 - Implemented by JA
	//v2.0 - JA (GPS)
	public Museum reconstructEdit(final Museum prunedMuseum, final BindingResult binding) {

		Assert.notNull(prunedMuseum);

		final Museum reconstructedMuseum;

		if (prunedMuseum.getId() == 0) {
			reconstructedMuseum = this.create();
			reconstructedMuseum.setIdentifier(prunedMuseum.getIdentifier());
			reconstructedMuseum.setName(prunedMuseum.getName());
			reconstructedMuseum.setAddress(prunedMuseum.getAddress());
			reconstructedMuseum.setCoordinates(prunedMuseum.getCoordinates());
			reconstructedMuseum.setEmail(prunedMuseum.getEmail());
			reconstructedMuseum.setPhoneNumber(prunedMuseum.getPhoneNumber());
			reconstructedMuseum.setPrice(prunedMuseum.getPrice());

		} else {
			final Museum retrievedMuseum = this.findOne(prunedMuseum.getId());
			Assert.notNull(retrievedMuseum);

			reconstructedMuseum = prunedMuseum;

			reconstructedMuseum.setIdentifier(retrievedMuseum.getIdentifier());
			reconstructedMuseum.setVersion(retrievedMuseum.getVersion());
			reconstructedMuseum.setDayPasses(retrievedMuseum.getDayPasses());
			reconstructedMuseum.setDirector(retrievedMuseum.getDirector());
			reconstructedMuseum.setGroups(retrievedMuseum.getGroups());
			reconstructedMuseum.setGuides(retrievedMuseum.getGuides());
			reconstructedMuseum.setReviews(retrievedMuseum.getReviews());
			reconstructedMuseum.setRooms(retrievedMuseum.getRooms());
		}

		if ("".equals(prunedMuseum.getSlogan()))
			reconstructedMuseum.setSlogan(null);
		else
			reconstructedMuseum.setSlogan(prunedMuseum.getSlogan());

		if ("".equals(prunedMuseum.getBanner()))
			reconstructedMuseum.setBanner(null);
		else
			reconstructedMuseum.setBanner(prunedMuseum.getBanner());

		this.validator.validate(reconstructedMuseum, binding);

		if (reconstructedMuseum.getCoordinates() != null) {

			final GPSCoordinates mCoord = reconstructedMuseum.getCoordinates();

			if (mCoord.getLatitude() == null)
				binding.rejectValue("coordinates.latitude", "javax.validation.constraints.NotNull.message", "Latitude is required");

			if (mCoord.getLongitude() == null)
				binding.rejectValue("coordinates.longitude", "javax.validation.constraints.NotNull.message", "Longitude is required");

		}

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

	// v2.0 - JA
	public Museum saveAddGuide(final Museum museum) {

		Assert.notNull(museum);
		Assert.isTrue(museum.getId() != 0);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Museum oldMuseum = this.findOne(museum.getId());

		Assert.isTrue(oldMuseum.getDirector().equals(museum.getDirector()));
		Assert.isTrue(museum.getDirector().equals(director));

		return this.save(museum);
	}

	// v1.0 - Alicia
	public Collection<Museum> getByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Museum> res = this.museumRepository.findByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}
}
