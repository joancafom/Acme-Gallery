
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

import repositories.RoomRepository;
import security.LoginService;
import domain.Director;
import domain.Exhibition;
import domain.Incident;
import domain.Museum;
import domain.Room;

@Service
@Transactional
public class RoomService extends ActorService {

	// Managed Repository ---------------------------------------------------------------------------------

	@Autowired
	private RoomRepository		roomRepository;

	// Supporting Services --------------------------------------------------------------------------------

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private ExhibitionService	exhibitionService;

	@Autowired
	private IncidentService		incidentService;

	@Autowired
	private MuseumService		museumService;

	// Validator --------------------------------------------------------------------------------------

	@Autowired
	private Validator			validator;


	//CRUD Methods ----------------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Room create() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Room room = new Room();

		room.setInRepair(false);

		room.setExhibitions(new HashSet<Exhibition>());
		room.setIncidents(new HashSet<Incident>());

		return room;
	}

	// v1.0 - Alicia
	public void delete(final Room room) {
		Assert.notNull(room);
		Assert.isTrue(this.roomRepository.exists(room.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(this.canBeDeleted(room));

		for (final Exhibition e : room.getExhibitions())
			this.exhibitionService.deleteRoom(e);

		room.getMuseum().getRooms().remove(room);
		this.museumService.save(room.getMuseum());

		for (final Incident i : room.getIncidents())
			this.incidentService.deleteRoom(i);

		this.roomRepository.delete(room);
	}

	// v1.0 - Alicia
	public Room findOne(final int roomId) {
		return this.roomRepository.findOne(roomId);
	}

	// v1.0 Alicia
	public Room save(final Room room) {
		Assert.notNull(room);

		return this.roomRepository.save(room);
	}

	// v1.0 - Alicia
	public Room saveCreateAndEdit(final Room room) {
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		Assert.isTrue(director.getMuseums().contains(room.getMuseum()));

		return this.roomRepository.save(room);
	}

	//Other Business Methods ------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Collection<Room> getByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Room> res = this.roomRepository.findByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Room> getByDirector(final Director director) {
		Assert.notNull(director);

		final Director principal = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.equals(principal));

		final Collection<Room> res = this.roomRepository.findByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Room> getByDirector(final Director director, final int page, final int size) {
		Assert.notNull(director);

		final Director principal = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.equals(principal));

		final Page<Room> res = this.roomRepository.findByDirectorId(director.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Room> getByMuseum(final Museum museum) {
		Assert.notNull(museum);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.getMuseums().contains(museum));

		final Collection<Room> res = this.roomRepository.findByMuseumId(museum.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Room> getByMuseum(final Museum museum, final int page, final int size) {
		Assert.notNull(museum);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(director.getMuseums().contains(museum));

		final Page<Room> res = this.roomRepository.findByMuseumId(museum.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Boolean canBeMarkedAsInRepair(final Room room) {
		Boolean res = false;

		if (!room.getInRepair())
			res = true;

		return res;
	}

	// v1.0 - Alicia
	public void inRepair(final Room room) {
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(room.getMuseum()));

		Assert.isTrue(this.canBeMarkedAsInRepair(room));

		room.setInRepair(true);

		this.roomRepository.save(room);
	}

	// v1.0 - Alicia
	public Boolean canBeMarkedAsNotInRepair(final Room room) {
		Boolean res = false;

		if (room.getInRepair())
			res = true;

		return res;
	}

	// v1.0 - Alicia
	public void notInRepair(final Room room) {
		Assert.notNull(room);

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(room.getMuseum()));

		Assert.isTrue(this.canBeMarkedAsNotInRepair(room));

		room.setInRepair(false);

		this.roomRepository.save(room);
	}

	// v1.0 - Alicia
	public Room reconstructSave(final Room prunedRoom, final BindingResult binding) {
		Assert.notNull(prunedRoom);

		final Room res = this.create();

		res.setName(prunedRoom.getName());
		res.setArea(prunedRoom.getArea());
		res.setMuseum(prunedRoom.getMuseum());

		this.validator.validate(res, binding);

		return res;
	}

	// v1.0 - Alicia
	public Boolean canBeDeleted(final Room room) {
		Boolean res = false;

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		if (director.getMuseums().contains(room.getMuseum()) && this.exhibitionService.getCurrentByRoom(room).isEmpty() && this.exhibitionService.getFutureExhibitionsWithDayPassesByRoom(room).isEmpty()
			&& this.exhibitionService.getFutureExhibitionsWithSponsorshipsByRoom(room).isEmpty())
			res = true;

		return res;
	}
}
