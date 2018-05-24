
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

import utilities.AbstractTest;
import domain.Visitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class VisitorServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private VisitorService	visitorService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// [UC-004] Register as a Visitor ----------------
	// v1.0 - Alicia

	@Test
	public void driverVisitorRegister() {

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
				// 1 - (+) A unauthenticated Actor successfully registers.
				null, "testName", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "FEMALE", "testVisitor1", null
			}, {
				// 2 - (+) A unauthenticated Actor successfully registers. (No address and no gender)
				null, "testName", "test surnames", "test@gmail.com", "698437295", null, null, "testVisitor2", null
			}, {
				// 3 - (-) A unauthenticated Actor tries to register with a duplicated username.
				null, "testName", "test surnames", "test@gmail.com", "+34685295035", "C/ Test nº1", null, "visitor1", DataIntegrityViolationException.class
			}, {
				// 4 - (-) A unauthenticated Actor tries to register with an empty email.
				null, "testName", "test surnames", "", "+34681904241", null, "OTHER", "testVisitor4", ConstraintViolationException.class
			}, {
				// 5 - (-) A unauthenticated Actor tries to register with an incorrect phone number.
				null, "testName", "test surnames", "test@gmail.com", "74747", "C/ Test nº1", "FEMALE", "testVisitor5", ConstraintViolationException.class
			}, {
				// 6 - (-) A Sponsor tries to register.
				"sponsor1", "testName", "test surnames", "test@gmail.com", "+34678435284", "C/ Test nº1", "FEMALE", "testVisitor6", RuntimeException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateVisitorRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateVisitorRegister(final String performer, final String name, final String surnames, final String email, final String phoneNumber, final String address, final String gender, final String userAndPass, final Class<?> expected) {
		Class<?> caught = null;

		this.authenticate(performer);

		try {

			// 1. Register as Visitor
			final Visitor visitorToRegister = this.visitorService.create();

			visitorToRegister.getUserAccount().setUsername(userAndPass);
			visitorToRegister.getUserAccount().setPassword(userAndPass);
			visitorToRegister.setName(name);
			visitorToRegister.setSurnames(surnames);
			visitorToRegister.setEmail(email);
			visitorToRegister.setPhoneNumber(phoneNumber);
			visitorToRegister.setAddress(address);
			visitorToRegister.setGender(gender);

			this.visitorService.register(visitorToRegister);
			this.visitorService.flush();

			// 2. Log in as that Visitor
			this.authenticate(userAndPass);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
