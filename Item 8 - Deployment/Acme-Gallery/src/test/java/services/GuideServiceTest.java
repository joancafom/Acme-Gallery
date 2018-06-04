
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
import domain.Guide;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class GuideServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private GuideService	guideService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// -------------------------------------------------------------------------------
	// [UC-011] Administrator register a Guide.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverGuideRegister() {

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
				// + 1) An administrator successfully registers a new Guide.
				"admin", "testGuide", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "OTHER", "testGuide1", null
			}, {
				// + 2) An administrator successfully registers a new Guide (no gender nor address).
				"admin", "testGuide", "test surnames", "test@gmail.com", "698437295", null, null, "testGuide2", null
			}, {
				// - 3) An administrator tries to register a new Guide with a duplicated userName.
				"admin", "testGuide", "test surnames", "test@gmail.com", "+34685295035", "C/ Test nº1", null, "guide1", DataIntegrityViolationException.class
			}, {
				// - 4) An administrator tries to register a new Guide with an empty email.
				"admin", "testGuide", "test surnames", "", "+34681904241", null, "FEMALE", "testGuide4", ConstraintViolationException.class
			}, {
				// - 5) An administrator tries to register a new Guide with an incorrect phone number.
				"admin", "testGuide", "test surnames", "test@gmail.com", "74747", "C/ Test nº1", "MALE", "testGuide5", ConstraintViolationException.class
			}, {
				// - 6) A Sponsor tries to register a new Guide.
				"sponsor1", "testGuide", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "OTHER", "testGuide6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateGuideRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateGuideRegister(final String performer, final String name, final String surnames, final String email, final String phoneNumber, final String address, final String gender, final String userAndPass, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Register a new Director
			final Guide guideToRegister = this.guideService.create();

			guideToRegister.getUserAccount().setUsername(userAndPass);
			guideToRegister.getUserAccount().setPassword(userAndPass);
			guideToRegister.setName(name);
			guideToRegister.setSurnames(surnames);
			guideToRegister.setEmail(email);
			guideToRegister.setPhoneNumber(phoneNumber);
			guideToRegister.setAddress(address);
			guideToRegister.setGender(gender);

			this.guideService.register(guideToRegister);
			this.guideService.flush();

			Assert.notNull(guideToRegister.getIncidents());
			Assert.isTrue(guideToRegister.getIncidents().isEmpty());
			Assert.notNull(guideToRegister.getExhibitions());
			Assert.isTrue(guideToRegister.getExhibitions().isEmpty());

			// 2. Log in as that Sponsor
			this.authenticate(userAndPass);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
