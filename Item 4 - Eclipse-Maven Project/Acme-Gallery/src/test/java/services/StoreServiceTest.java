
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
import domain.Store;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class StoreServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private StoreService	storeService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private MuseumService	museumService;


	// [UC-036] Director create a Store ------------------------
	// v1.0 - Alicia

	@Test
	public void driverCreateStore() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> name of the Store.
		// testingData[i][2] -> logo of the Store.
		// testingData[i][3] -> phoneNumber of the Store.
		// testingData[i][4] -> email of the Store.
		// testingData[i][5] -> museum of the Store.
		// testingData[i][6] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Director correctly creates a Store
				"director1", "testName", "http://www.logo.es", "+34685839512", "test@email.com", "museum4", null
			}, {
				// 2 - (-) A Director creates a Store for a Museum she doesn't own
				"director1", "testName", "http://www.logo.es", "+34685839512", "test@email.com", "museum9", IllegalArgumentException.class
			}, {
				// 3 - (-) A Director creates a Store for a null Museum.
				"director1", "testName", "http://www.logo.es", "+34685839512", "test@email.com", null, IllegalArgumentException.class
			}, {
				// 4 - (-) A Director creates a Store for a Museum that already has a Store.
				"director1", "testName", "http://www.logo.es", "+34685839512", "test@email.com", "museum1", IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor creates a Store.
				"visitor1", "testName", "http://www.logo.es", "+34685839512", "test@email.com", "museum4", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Museum museum = null;

			if (testingData[i][5] != null)
				museum = this.museumService.findOne(super.getEntityId((String) testingData[i][5]));

			this.startTransaction();

			this.templateCreateStore((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], museum, (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateCreateStore(final String username, final String name, final String logo, final String phoneNumber, final String email, final Museum museum, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Create a Store

			final Store createdStore = this.storeService.create(museum);

			createdStore.setName(name);
			createdStore.setLogo(logo);
			createdStore.setPhoneNumber(phoneNumber);
			createdStore.setEmail(email);
			createdStore.setMuseum(museum);

			final Store savedStore = this.storeService.save(createdStore);

			// Flush
			this.storeService.flush();

			// 3. Check it has been created

			Assert.isTrue(savedStore.equals(museum.getStore()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// [UC-037] Director delete a Store ------------------------
	// v1.0 - Alicia

	@Test
	public void driverDeleteStore() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Store to delete.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Director correctly deletes a Store
				"director1", "store5", null
			}, {
				// 2 - (-) A Director deletes a Store she doesn't own
				"director1", "store7", IllegalArgumentException.class
			}, {
				// 3 - (-) A Director deletes a null Store
				"director1", null, IllegalArgumentException.class
			}, {
				// 4 - (-) A Director deletes a non-existing Store
				"director1", 1536, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor deletes a Store.
				"visitor1", "store5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Store store = null;

			if (testingData[i][1] != null && i != 3)
				store = this.storeService.findOne(super.getEntityId((String) testingData[i][1]));
			else if (i == 3) {
				store = new Store();
				store.setId((Integer) testingData[i][1]);
			}

			this.startTransaction();

			this.templateDeleteStore((String) testingData[i][0], store, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateDeleteStore(final String username, final Store store, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Delete a Store

			this.storeService.delete(store);

			// Flush
			this.storeService.flush();

			// 3. Check it has been deleted

			Assert.isNull(this.storeService.findOne(store.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

}
