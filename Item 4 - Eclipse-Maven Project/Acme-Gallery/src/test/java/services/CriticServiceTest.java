
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
import domain.Critic;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CriticServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private CriticService	criticService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// -------------------------------------------------------------------------------
	// [UC-009] Administrator register a Critic.
	// 
	// Related requirements:
	//   · REQ 1: The actors of the system are administrators, directors, visitors, sponsors, guides 
	//			  and critics. For every actor, the system must store a name, the surnames, an email,
	//            a phone number (which must follow the pattern: optional plus sign and a sequence of 
	//			  numbers), an optional address and optional gender (which can be male, female, or other).
	//
	//   · REQ 22.9: An actor who is authenticated as an administrator must be able to
	//                Create an account for a new critic
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverCriticRegister() {

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
				// + 1) An administrator successfully registers a new Critic.
				"admin", "testCritic", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "OTHER", "testCritic1", null
			}, {
				// + 2) An administrator successfully registers a new Critic (no gender nor address).
				"admin", "testCritic", "test surnames", "test@gmail.com", "698437295", null, null, "testCritic2", null
			}, {
				// - 3) An administrator tries to register a new Critic with a duplicated userName.
				"admin", "testCritic", "test surnames", "test@gmail.com", "+34685295035", "C/ Test nº1", null, "critic1", DataIntegrityViolationException.class
			}, {
				// - 4) An administrator tries to register a new Critic with an empty email.
				"admin", "testCritic", "test surnames", "", "+34681904241", null, "FEMALE", "testCritic4", ConstraintViolationException.class
			}, {
				// - 5) An administrator tries to register a new Critic with an incorrect phone number.
				"admin", "testCritic", "test surnames", "test@gmail.com", "74747", "C/ Test nº1", "MALE", "testCritic5", ConstraintViolationException.class
			}, {
				// - 6) A Sponsor tries to register a new Critic.
				"sponsor1", "testCritic", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "OTHER", "testCritic6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateCriticRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateCriticRegister(final String performer, final String name, final String surnames, final String email, final String phoneNumber, final String address, final String gender, final String userAndPass, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Register a new Critic
			final Critic criticToRegister = this.criticService.create();

			criticToRegister.getUserAccount().setUsername(userAndPass);
			criticToRegister.getUserAccount().setPassword(userAndPass);
			criticToRegister.setName(name);
			criticToRegister.setSurnames(surnames);
			criticToRegister.setEmail(email);
			criticToRegister.setPhoneNumber(phoneNumber);
			criticToRegister.setAddress(address);
			criticToRegister.setGender(gender);

			this.criticService.register(criticToRegister);
			this.criticService.flush();

			Assert.notNull(criticToRegister.getCritiques());
			Assert.isTrue(criticToRegister.getCritiques().isEmpty());

			// 2. Log in as that Sponsor
			this.authenticate(userAndPass);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
