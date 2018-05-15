
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RoomRepository;
import security.LoginService;
import domain.Director;
import domain.Room;

@Service
@Transactional
public class RoomService extends ActorService {

	// Managed Repository ---------------------------------------------------------------------------------

	@Autowired
	private RoomRepository	roomRepository;

	// Supporting Services --------------------------------------------------------------------------------

	@Autowired
	private DirectorService	directorService;


	//CRUD Methods ----------------------------------------------------------------------------------------

	public Room save(final Room room) {
		Assert.notNull(room);

		return this.roomRepository.save(room);
	}

	//Other Business Methods ------------------------------------------------------------------------------

	public Collection<Room> getAvailableByPrincipal() {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);

		final Collection<Room> res = this.roomRepository.findAvailableByDirectorId(director.getId());
		Assert.notNull(res);

		return res;
	}

}
