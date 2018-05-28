
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.IncidentRepository;
import security.LoginService;
import domain.Director;
import domain.Guide;
import domain.Incident;
import domain.Museum;
import domain.Room;

@Service
@Transactional
public class IncidentService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private IncidentRepository	incidentRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private GuideService		guideService;

	@Autowired
	private RoomService			roomService;

	// Validator --------------------------------------------------------------------------------------

	@Autowired
	private Validator			validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - JA
	public Incident create() {

		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(guide);

		final Incident res = new Incident();

		res.setGuide(guide);
		res.setIsChecked(false);

		return res;

	}

	// v1.0 - JA
	public Incident save(final Incident incident) {

		Assert.notNull(incident);

		return this.incidentRepository.save(incident);
	}

	// v1.0 - Alicia
	public void delete(final Incident incident) {
		Assert.notNull(incident);
		Assert.isTrue(this.incidentRepository.exists(incident.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(incident.getRoom().getMuseum()));

		Assert.isTrue(!incident.getIsChecked());

		incident.getRoom().getIncidents().remove(incident);
		this.roomService.save(incident.getRoom());

		incident.getGuide().getIncidents().remove(incident);
		this.guideService.save(incident.getGuide());

		this.incidentRepository.delete(incident);
	}

	// v1.0 - Alicia
	public void deleteRoom(final Incident incident) {
		Assert.notNull(incident);
		Assert.isTrue(this.incidentRepository.exists(incident.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(incident.getRoom().getMuseum()));

		incident.getGuide().getIncidents().remove(incident);
		this.guideService.save(incident.getGuide());

		this.incidentRepository.delete(incident);
	}

	// v1.0 - Alicia
	public Incident findOne(final int incidentId) {
		return this.incidentRepository.findOne(incidentId);
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - Alicia
	public Collection<Incident> getByDirector(final Director director) {
		Assert.notNull(director);

		final Director principal = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.equals(principal));

		final Collection<Incident> res = this.incidentRepository.findByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Incident> getByDirector(final Director director, final int page, final int size) {
		Assert.notNull(director);

		final Director principal = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.equals(principal));

		final Page<Incident> res = this.incidentRepository.findByDirectorId(director.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Incident> getByMuseum(final Museum museum) {
		Assert.notNull(museum);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.getMuseums().contains(museum));

		final Collection<Incident> res = this.incidentRepository.findByMuseumId(museum.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Incident> getByMuseum(final Museum museum, final int page, final int size) {
		Assert.notNull(museum);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());

		Assert.isTrue(director != null || guide != null);

		if (director != null)
			Assert.isTrue(director.getMuseums().contains(museum));
		else
			Assert.isTrue(museum.getGuides().contains(guide));

		final Page<Incident> res = this.incidentRepository.findByMuseumId(museum.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}
	// v1.0 - Alicia
	public void check(final Incident incident) {
		Assert.notNull(incident);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(incident.getRoom().getMuseum()));

		Assert.isTrue(!incident.getIsChecked());

		incident.setIsChecked(true);

		this.incidentRepository.save(incident);
	}

	// v1.0 - Alicia
	public Collection<Incident> getByRoom(final Room room) {
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.getMuseums().contains(room.getMuseum()));

		final Collection<Incident> res = this.incidentRepository.findByRoomId(room.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Incident> getByRoom(final Room room, final int page, final int size) {
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.getMuseums().contains(room.getMuseum()));

		final Page<Incident> res = this.incidentRepository.findByRoomId(room.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by JA
	public Incident reconstructCreate(final Incident prunedIncident, final BindingResult binding) {

		Assert.notNull(prunedIncident);

		final Incident res = this.create();

		res.setText(prunedIncident.getText());
		res.setLevel(prunedIncident.getLevel());
		res.setRoom(prunedIncident.getRoom());

		this.validator.validate(res, binding);

		return res;
	}

	//v1.0 - Implemented by JA
	public Incident saveCreate(final Incident incident) {

		Assert.notNull(incident);
		Assert.isTrue(incident.getId() == 0);
		Assert.notNull(incident.getGuide());
		Assert.notNull(incident.getRoom());
		Assert.notNull(incident.getRoom().getMuseum());

		final Guide guide = this.guideService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(guide);

		Assert.isTrue(incident.getRoom().getMuseum().getGuides().contains(guide));
		Assert.isTrue(!incident.getIsChecked());

		return this.save(incident);
	}
}
