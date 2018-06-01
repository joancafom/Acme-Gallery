
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Director;
import domain.Museum;
import domain.Room;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RoomServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private RoomService		roomService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private MuseumService	museumService;

	@Autowired
	private DirectorService	directorService;


	// -------------------------------------------------------------------------------
	// [UC-031] Director delete a room.
	// 
	// Related requirements:
	//
	//   · REQ 23.29: Delete a room, as long as it has no current exhibition and its future
	//				  exhibitions haven't sold day passes or sponsorships.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverDeleteRoom() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the room to delete.
		// testingData[i][2] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A director successfully deletes room of one of her or his museums, having
				//      neither current nor future exhibitions.
				"director1", "room16", null
			}, {
				// - 2) A director tries to delete a room with current a exhibition.
				"director1", "room1", IllegalArgumentException.class
			}, {
				// - 3) A director tries to delete a room with future exhibitions.
				"director1", "room7", IllegalArgumentException.class
			}, {
				// - 4) A director tries to delete a room with future exhibitions with day passes.
				"director1", "room12", IllegalArgumentException.class
			}, {
				// - 5) A director tries to delete the room of a museum she or he does not own.
				"director2", "room16", IllegalArgumentException.class
			}
		};

		Room room;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				room = this.roomService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				room = null;

			this.startTransaction();

			this.templateDeleteRoom((String) testingData[i][0], room, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateDeleteRoom(final String performer, final Room room, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			Museum museumRoom = null;

			if (room != null)
				museumRoom = room.getMuseum();

			// 1. Delete the room
			this.roomService.delete(room);
			this.roomService.flush();

			//2. Get the list of rooms of its museum and Assert it does not longer appear
			final Museum museumAfter = this.museumService.findOne(museumRoom.getId());

			Assert.isTrue(!museumAfter.getRooms().contains(room));

			//3. Furthermore, the product must not be in the system
			Assert.isNull(this.roomService.findOne(room.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-030] - Director create a room
	 * 1. Log in as director
	 * 2. List my rooms
	 * 3. Create a room
	 * 
	 * REQ: 4, 29.13
	 * 
	 * v1.0 - josembell
	 */
	@Test
	public void driverCreateRoom() {

		final Object testingData[][] = {
			{
				/* + 1. Un director crea un room para un museo suyo */
				"director1", "museum1", "TEST", 10.50, null
			}, {
				/* - 2. Un usuario no identificado crea un room */
				null, "museum1", "TEST", 10.50, IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es director crea un room */
				"visitor1", "museum1", "TEST", 10.50, IllegalArgumentException.class
			}, {
				/* - 4. Un director crea un room para un museum que no es suyo */
				"director1", "museum7", "TEST", 10.50, IllegalArgumentException.class
			}, {
				/* - 5. Un director crea un room para un museum null */
				"director1", null, "TEST", 10.50, IllegalArgumentException.class
			}, {
				/* - 6. Un director crea un room sin nombre */
				"director1", "museum1", null, 10.50, ConstraintViolationException.class
			}, {
				/* - 7. Un director crea una room con area invalida */
				"director1", "museum1", "TEST", 0.00, ConstraintViolationException.class
			}, {
				/* - 8. Un director crea una room sin area */
				"director1", "museum1", "TEST", null, ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			Museum museum = null;
			if (testingData[i][1] != null)
				museum = this.museumService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			this.templateCreateRoom((String) testingData[i][0], museum, (String) testingData[i][2], (Double) testingData[i][3], (Class<?>) testingData[i][4]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	private void templateCreateRoom(final String username, final Museum museum, final String name, final Double area, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 3. create room */
			final Room room = this.roomService.create();
			room.setArea(area);
			room.setName(name);
			room.setMuseum(museum);

			final Room saved = this.roomService.saveCreateAndEdit(room);

			/* 2. list my rooms */
			final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
			final Collection<Room> myRooms = this.roomService.getByDirector(director);
			Assert.isTrue(myRooms.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-032] - Director mark room in repair
	 * 1. Log in as director
	 * 2. List my rooms
	 * 3. Display a room
	 * 4. Mark the room in repair
	 * 
	 * REQ: 4, 29.17
	 * 
	 * v1.0 - josembell
	 */
	@Test
	public void driverMarkRoomInRepair() {

		final Object testingData[][] = {
			{
				/* + 1. Un director marca un room suyo en reparación */
				"director1", "room1", null
			}, {
				/* - 2. Un usuario no identificado marca una room en reparacion */
				null, "room1", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es director marca una room en reparacion */
				"visitor1", "room1", IllegalArgumentException.class
			}, {
				/* - 4. Un director marca una room que no es suya en reparación */
				"director1", "room22", IllegalArgumentException.class
			}, {
				/* - 5. Un director marca una room null en reparación */
				"director1", null, IllegalArgumentException.class
			}, {
				/* - 6. Un director marca una room en reparación que ya lo estaba */
				"director1", "room5", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			Room room = null;
			if (testingData[i][1] != null)
				room = this.roomService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			this.templateMarkRoomInRepair((String) testingData[i][0], room, (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateMarkRoomInRepair(final String username, final Room room, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 4. mark the room in repair */
			this.roomService.inRepair(room);
			this.roomService.flush();

			final Room marked = this.roomService.findOne(room.getId());
			Assert.isTrue(marked.getInRepair() == true);

			/* 2. list my rooms */
			final Collection<Room> myRooms = this.roomService.getByPrincipal();
			Assert.isTrue(myRooms.contains(room));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-033] - Director mark room in repair
	 * 1. Log in as director
	 * 2. List my rooms
	 * 3. Display a room
	 * 4. Mark the room in repair
	 * 
	 * REQ: 4, 29.17
	 * 
	 * v1.0 - josembell
	 */
	@Test
	public void driverMarkRoomNotInRepair() {

		final Object testingData[][] = {
			{
				/* + 1. Un director marca un room suyo en no reparación */
				"director1", "room5", null
			}, {
				/* - 2. Un usuario no identificado marca una room en no reparacion */
				null, "room5", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es director marca una room en no reparacion */
				"visitor1", "room5", IllegalArgumentException.class
			}, {
				/* - 4. Un director marca una room que no es suya en no reparación */
				"director1", "room23", IllegalArgumentException.class
			}, {
				/* - 5. Un director marca una room null en no reparación */
				"director1", null, IllegalArgumentException.class
			}, {
				/* - 6. Un director marca una room en no reparación que ya lo estaba */
				"director1", "room1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			Room room = null;
			if (testingData[i][1] != null)
				room = this.roomService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			this.templateMarkRoomNotInRepair((String) testingData[i][0], room, (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateMarkRoomNotInRepair(final String username, final Room room, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 4. mark the room in repair */
			this.roomService.notInRepair(room);
			this.roomService.flush();

			final Room marked = this.roomService.findOne(room.getId());
			Assert.isTrue(marked.getInRepair() == false);

			/* 2. list my rooms */
			final Collection<Room> myRooms = this.roomService.getByPrincipal();
			Assert.isTrue(myRooms.contains(room));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

}
