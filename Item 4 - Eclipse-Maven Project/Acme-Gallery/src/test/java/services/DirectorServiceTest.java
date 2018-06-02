
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Director;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DirectorServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private DirectorService	directorService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// -------------------------------------------------------------------------------
	// [UC-010] Administrator register a Director.
	// 
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverDirectorRegister() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the name of the user.
		// testingData[i][2] -> the surnames of the user.
		// testingData[i][3] -> the email of the user.
		// testingData[i][4] -> the phoneNumber of the user.
		// testingData[i][5] -> the address of the user.
		// testingData[i][6] -> the gender of the user.
		// testingData[i][7] -> the username/password of the user.
		// testingData[i][8] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) An administrator successfully registers a new Director.
				"admin", "testDirector", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "OTHER", "testDirector1", null
			}, {
				// + 2) An administrator successfully registers a new Director (no gender nor address).
				"admin", "testDirector", "test surnames", "test@gmail.com", "698437295", null, null, "testDirector2", null
			}, {
				// - 3) An administrator tries to register a new Director with a duplicated userName.
				"admin", "testDirector", "test surnames", "test@gmail.com", "+34685295035", "C/ Test nº1", null, "director1", DataIntegrityViolationException.class
			}, {
				// - 4) An administrator tries to register a new Director with an empty email.
				"admin", "testDirector", "test surnames", "", "+34681904241", null, "FEMALE", "testDirector4", ConstraintViolationException.class
			}, {
				// - 5) An administrator tries to register a new Director with an incorrect phone number.
				"admin", "testDirector", "test surnames", "test@gmail.com", "74747", "C/ Test nº1", "MALE", "testDirector5", ConstraintViolationException.class
			}, {
				// - 6) A Sponsor tries to register a new Director.
				"sponsor1", "testDirector", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "OTHER", "testDirector6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateDirectorRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateDirectorRegister(final String performer, final String name, final String surnames, final String email, final String phoneNumber, final String address, final String gender, final String userAndPass, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Register a new Director
			final Director directorToRegister = this.directorService.create();

			directorToRegister.getUserAccount().setUsername(userAndPass);
			directorToRegister.getUserAccount().setPassword(userAndPass);
			directorToRegister.setName(name);
			directorToRegister.setSurnames(surnames);
			directorToRegister.setEmail(email);
			directorToRegister.setPhoneNumber(phoneNumber);
			directorToRegister.setAddress(address);
			directorToRegister.setGender(gender);

			this.directorService.register(directorToRegister);
			this.directorService.flush();

			Assert.notNull(directorToRegister.getMuseums());
			Assert.isTrue(directorToRegister.getMuseums().isEmpty());

			// 2. Log in as that Sponsor
			this.authenticate(userAndPass);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-052] Display Director.
	// 
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverDisplayDirector() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the director to display.
		// testingData[i][2] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) An unauthenticated user displays a director
				null, "director1", null
			}, {
				// + 2) An authenticated user displays a director
				"visitor1", "director3", null
			}, {
				// - 3) An authenticated user tries to display a null director
				"visitor1", null, NullPointerException.class
			}, {
				// - 4) An authenticated user tries to display a non-existing director
				"visitor1", "directorPrueba", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateDirectorDisplay((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateDirectorDisplay(final String performer, final String directorToDisplay, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Display a Director
			final Integer idToRetrieve = this.getEntityId(directorToDisplay);
			final Director director = this.directorService.findOne(idToRetrieve);

			Assert.notNull(director);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
