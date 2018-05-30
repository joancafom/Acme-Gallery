
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

import utilities.AbstractTest;
import domain.GPSCoordinates;
import domain.Museum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MuseumServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private MuseumService	museumService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// [UC-026] Director create Museum ---------------
	// v1.0 - Alicia

	@Test
	public void driverCreateMuseum() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> name of the Museum.
		// testingData[i][2] -> address of the Museum.
		// testingData[i][3] -> latitude of the Museum.
		// testingData[i][4] -> longitude of the Museum.
		// testingData[i][5] -> email of the Museum.
		// testingData[i][6] -> phoneNumber of the Museum.
		// testingData[i][7] -> slogan of the Museum.
		// testingData[i][8] -> identifier of the Museum.
		// testingData[i][9] -> banner of the Museum.
		// testingData[i][10] -> price of the Museum.
		// testingData[i][11] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Director correctly creates a Museum
				"director1", "testName", "C/ Test n�29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, null
			}, {
				// 2 - (-) A Director creates a Museum with an existing identifier.
				"director1", "testName", "C/ Test n�29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "TBM", "http://www.google.es", 3.0, DataIntegrityViolationException.class
			}, {
				// 3 - (-) A Director creates a Museum with a null latitude.
				"director1", "testName", "C/ Test n�29", null, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, IllegalArgumentException.class
			}, {
				// 4 - (-) A Director creates a Museum with a phoneNumber with spaces. 
				"director1", "testName", "C/ Test n�29", 80.123, -45.434, "test@email.com", "+34 698245143", "testSlogan", "POP", "http://www.google.es", 3.0, ConstraintViolationException.class
			}, {
				// 5 - (-) A Visitor creates a Museum.
				"visitor1", "testName", "C/ Test n�29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			this.templateCreateMuseum((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (Double) testingData[i][10], (Class<?>) testingData[i][11]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateCreateMuseum(final String username, final String name, final String address, final Double latitude, final Double longitude, final String email, final String phoneNumber, final String slogan, final String identifier,
		final String banner, final Double price, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List my Museums

			final Collection<Museum> oldMuseums = this.museumService.getByPrincipal();

			// 3. Create a Museum

			final Museum createdMuseum = this.museumService.create();

			final GPSCoordinates gpsCoordinates = new GPSCoordinates();
			gpsCoordinates.setLatitude(latitude);
			gpsCoordinates.setLongitude(longitude);

			createdMuseum.setAddress(address);
			createdMuseum.setBanner(banner);
			createdMuseum.setCoordinates(gpsCoordinates);
			createdMuseum.setEmail(email);
			createdMuseum.setIdentifier(identifier);
			createdMuseum.setName(name);
			createdMuseum.setPhoneNumber(phoneNumber);
			createdMuseum.setPrice(price);
			createdMuseum.setSlogan(slogan);

			final Museum savedMuseum = this.museumService.saveCreate(createdMuseum);

			// Flush
			this.museumService.flush();

			// 3. Check the Museums

			final Collection<Museum> newMuseums = this.museumService.getByPrincipal();

			Assert.isTrue(newMuseums.contains(savedMuseum));
			Assert.isTrue(newMuseums.size() > oldMuseums.size());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
