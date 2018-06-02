
package services;

import java.util.Collection;

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

import security.LoginService;
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

	// [UC-018] Administrator ban a Visitor ----------
	// v1.0 - Alicia

	@Test
	public void driverBanVisitor() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Visitor to ban.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) An Administrator correctly bans a Visitor.
				"admin", "visitor6", null
			}, {
				// 2 - (-) An Administrator bans an already banned Visitor.
				"admin", "visitor7", IllegalArgumentException.class
			}, {
				// 3 - (-) An Administrator bans a non-existing Visitor.
				"admin", "abcdefg", IllegalArgumentException.class
			}, {
				// 4 - (-) An Administrator bans a null Visitor.
				"admin", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Sponsor bans a Visitor.
				"sponsor1", "visitor6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Visitor visitor = null;

			if (i < 2 || i == 4)
				visitor = this.visitorService.findOne(super.getEntityId((String) testingData[i][1]));

			if (i == 2)
				visitor = new Visitor();

			this.startTransaction();

			this.templateBanVisitor((String) testingData[i][0], visitor, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateBanVisitor(final String username, final Visitor visitor, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			Boolean isBanned = null;

			if (visitor != null && visitor.getUserAccount() != null)
				isBanned = visitor.getUserAccount().getIsLocked();

			// 2. List Visitors
			final Collection<Visitor> oldBannedVisitors = this.visitorService.findAllLocked();
			final Collection<Visitor> oldNonBannedVisitors = this.visitorService.findAllUnlocked();

			// 3. Ban a Visitor
			this.visitorService.ban(visitor);

			// Flush
			this.visitorService.flush();

			// 4. List again and check
			final Collection<Visitor> newBannedVisitors = this.visitorService.findAllLocked();
			final Collection<Visitor> newNonBannedVisitors = this.visitorService.findAllUnlocked();

			if (!isBanned) {
				Assert.isTrue(!oldBannedVisitors.contains(visitor));
				Assert.isTrue(newBannedVisitors.contains(visitor));

				Assert.isTrue(oldNonBannedVisitors.contains(visitor));
				Assert.isTrue(!newNonBannedVisitors.contains(visitor));

			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * [UC-051] - Visitor edit my profile
	 * v1.0 - josembell
	 */
	@Test
	public void driverEditProfile() {
		final Object testingData[][] = {
			{
				/* + 1. Un visitor edita su perfil correctamente */
				"visitor1", "Test", "Tests", "test@gmail.com", "+34651980700", "Av. Test 127", null
			}, {
				/* - 2. Un usuario no identificado edita su perfil */
				null, "Test", "Tests", "test@gmail.com", "+34651980700", "Av. Test 127", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es visitor edita su perfil */
				"director1", "Test", "Tests", "test@gmail.com", "+34651980700", "Av. Test 127", IllegalArgumentException.class
			}, {
				/* - 4. Un visitor edita su perfil sin introducir datos */
				"visitor1", null, null, null, null, null, ConstraintViolationException.class
			}, {
				/* - 5. Un visitor edita su perfil con datos erróneos */
				"visitor1", "Test", "Testes", "test", "test", "Test", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();

			this.templateEditProfile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	/* v1.0 - josembell */
	protected void templateEditProfile(final String username, final String name, final String surnames, final String email, final String phone, final String address, final Class<?> expected) {
		Class<?> caught = null;
		/* authenticate */
		this.authenticate(username);

		try {

			/* display profile */
			final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());

			/* edit data */
			try {
				visitor.setName(name);
				visitor.setSurnames(surnames);
				visitor.setEmail(email);
				visitor.setPhoneNumber(phone);
				visitor.setAddress(address);
			} catch (final Throwable oops) {

			}

			/* save & flush */
			this.visitorService.save(visitor);
			this.visitorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
