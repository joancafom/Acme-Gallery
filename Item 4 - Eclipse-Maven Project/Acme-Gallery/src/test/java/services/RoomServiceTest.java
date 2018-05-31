
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
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
}
