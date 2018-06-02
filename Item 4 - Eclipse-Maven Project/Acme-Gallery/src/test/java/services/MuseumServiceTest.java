
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
import org.springframework.validation.BeanPropertyBindingResult;

import utilities.AbstractTest;
import domain.Director;
import domain.GPSCoordinates;
import domain.Guide;
import domain.Museum;
import forms.MuseumForm;

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

	@Autowired
	private DirectorService	directorService;

	@Autowired
	private GuideService	guideService;


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
				"director1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, null
			}, {
				// 2 - (-) A Director creates a Museum with an existing identifier.
				"director1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "TBM", "http://www.google.es", 3.0, DataIntegrityViolationException.class
			}, {
				// 3 - (-) A Director creates a Museum with a null latitude.
				"director1", "testName", "C/ Test nº29", null, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, IllegalArgumentException.class
			}, {
				// 4 - (-) A Director creates a Museum with a phoneNumber with spaces. 
				"director1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34 698245143", "testSlogan", "POP", "http://www.google.es", 3.0, ConstraintViolationException.class
			}, {
				// 5 - (-) A Visitor creates a Museum.
				"visitor1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, IllegalArgumentException.class
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

	// [UC-027] Director edit a Museum -------------------------
	// v1.0 - Alicia

	@Test
	public void driverEditMuseum() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Museum to edit.
		// testingData[i][2] -> name of the Museum.
		// testingData[i][3] -> address of the Museum.
		// testingData[i][4] -> latitude of the Museum.
		// testingData[i][5] -> longitude of the Museum.
		// testingData[i][6] -> email of the Museum.
		// testingData[i][7] -> phoneNumber of the Museum.
		// testingData[i][8] -> slogan of the Museum.
		// testingData[i][9] -> identifier of the Museum.
		// testingData[i][10] -> banner of the Museum.
		// testingData[i][11] -> price of the Museum.
		// testingData[i][12] -> director of the Museum.
		// testingData[i][13] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Director correctly edits a Museum
				"director1", "museum1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", null, "http://www.google.es", 3.0, null, null
			}, {
				// 2 - (-) A Director tries to edit the identifier of a Museum
				"director1", "museum1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", "POP", "http://www.google.es", 3.0, null, IllegalArgumentException.class
			}, {
				// 3 - (-) A Director tries to edit a Museum that isn't hers
				"director1", "museum7", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", null, "http://www.google.es", 3.0, "director1", IllegalArgumentException.class
			}, {
				// 4 - (-) A Director tries to edit a non-existing Museum 
				"director1", "abcdefg", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34 698245143", "testSlogan", null, "http://www.google.es", 3.0, null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor tries to edit a Museum
				"visitor1", "museum1", "testName", "C/ Test nº29", 80.123, -45.434, "test@email.com", "+34698245143", "testSlogan", null, "http://www.google.es", 3.0, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Museum museum = null;

			if (i == 3)
				museum = new Museum();
			else
				museum = this.museumService.findOne(super.getEntityId((String) testingData[i][1]));

			Director director = null;

			if (testingData[i][12] != null)
				director = this.directorService.findOne(super.getEntityId((String) testingData[i][12]));

			this.startTransaction();

			this.templateEditMuseum((String) testingData[i][0], museum, (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Double) testingData[i][11], director, (Class<?>) testingData[i][13]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateEditMuseum(final String username, final Museum museum, final String name, final String address, final Double latitude, final Double longitude, final String email, final String phoneNumber, final String slogan,
		final String identifier, final String banner, final Double price, final Director director, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			final String oldAddress = museum.getAddress();

			// 2. Edit a Museum

			final GPSCoordinates gpsCoordinates = new GPSCoordinates();
			gpsCoordinates.setLatitude(latitude);
			gpsCoordinates.setLongitude(longitude);

			museum.setAddress(address);
			museum.setBanner(banner);
			museum.setCoordinates(gpsCoordinates);
			museum.setEmail(email);
			museum.setName(name);
			museum.setPhoneNumber(phoneNumber);
			museum.setPrice(price);
			museum.setSlogan(slogan);

			if (identifier != null)
				museum.setIdentifier(identifier);

			if (director != null)
				museum.setDirector(director);

			// 3. Save the Museum

			final Museum savedMuseum = this.museumService.saveEdit(museum);

			// Flush
			this.museumService.flush();

			// 3. Check the changes

			Assert.isTrue(!oldAddress.equals(savedMuseum.getAddress()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * [UC-060] - Add guide to a museum
	 * v1.0 - josembell
	 */
	@Test
	public void driverAddGuide() {
		final Object testingData[][] = {
			{
				/* + 1. Un director añade un guia a un museo suyo */
				"director1", "museum1", "guide7", null
			}, {
				/* - 2. Un usuario no identificado añade un guia a un museo */
				null, "museum1", "guide7", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es un director añade un guia a un museo */
				"visitor1", "museum1", "guide7", IllegalArgumentException.class
			}, {
				/* - 4. Un director añade un guia a un museo que no es suyo */
				"director1", "museum8", "guide7", IllegalArgumentException.class
			}, {
				/* - 5. Un director añade un guia que ya está en un museo suyo */
				"director1", "museum1", "guide1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final Museum museum = this.museumService.findOne(this.getEntityId((String) testingData[i][1]));
			final Guide guide = this.guideService.findOne(this.getEntityId((String) testingData[i][2]));
			this.startTransaction();
			//System.out.println(i);
			this.templateAddGuide((String) testingData[i][0], museum, guide, (Class<?>) testingData[i][3]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	/* v1.0 - josembell */
	protected void templateAddGuide(final String username, final Museum museum, final Guide guide, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 2. list my museums */
			final Collection<Museum> myMuseums = this.museumService.getByPrincipal();

			/* 3. add the guide */
			final MuseumForm museumForm = new MuseumForm();
			final List<String> guides = new LinkedList<String>();
			guides.add(String.valueOf(guide.getId()));

			museumForm.setMuseum(museum);
			museumForm.setGuides(guides);

			final Museum reconstructed = this.museumService.reconstructAddGuide(museumForm, new BeanPropertyBindingResult(museumForm, ""));
			final Museum saved = this.museumService.saveAddGuide(reconstructed);
			this.museumService.flush();

			/* 4. check that the museum is in the list */
			Assert.isTrue(myMuseums.contains(museum));
			Assert.isTrue(saved.getGuides().contains(guide));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
