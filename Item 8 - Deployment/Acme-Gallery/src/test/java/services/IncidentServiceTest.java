
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
import domain.Incident;
import domain.Room;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class IncidentServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private IncidentService	incidentService;

	//Helping Services

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private DirectorService	directorService;

	@Autowired
	private RoomService		roomService;


	/*
	 * [UC-038] - Guide create Incident
	 * v1.0 - josembell
	 */
	@Test
	public void driverCreateIncident() {

		final Object testingData[][] = {
			{
				/* + 1. Un guide crea un incident para una room de un museo que le pertenece */
				"guide1", "room5", "Test", "LOW", null
			}, {
				/* - 2. Un usuario no identificado crea un incident */
				null, "room5", "Test", "LOW", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es director crea un incident */
				"visitor1", "room5", "Test", "LOW", IllegalArgumentException.class
			}, {
				/* - 4. Un guide crea un incident sin texto */
				"guide1", "room5", null, "LOW", ConstraintViolationException.class
			}, {
				/* - 5. Un guide crea un incident sin level */
				"guide1", "room5", "TEST", null, ConstraintViolationException.class
			}, {
				/* - 6. Un guide crea un incident con level erróneo */
				"guide1", "room5", "TEST", "TEST", ConstraintViolationException.class
			}, {
				/* - 7. Un guide crea un incident para un room que no es suyo */
				"guide1", "room22", "TEST", "LOW", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			Room room = null;
			if (testingData[i][1] != null)
				room = this.roomService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			//System.out.println(i);
			this.templateCreateIncident((String) testingData[i][0], room, (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			//System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateCreateIncident(final String username, final Room room, final String text, final String level, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 5. create the incident */
			final Incident incident = this.incidentService.create();
			incident.setLevel(level);
			incident.setText(text);
			incident.setRoom(room);

			final Incident saved = this.incidentService.saveCreate(incident);
			this.incidentService.flush();
			/* 4. list my incidents */
			final Collection<Incident> incidents = this.incidentService.getByMuseum(room.getMuseum());
			Assert.isTrue(incidents.contains(saved));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-024] - Director check an Incident
	 * v1.0 - josembell
	 */
	@Test
	public void driverCheckIncident() {

		final Object testingData[][] = {
			{
				/* + 1. Un director marca un indicent suyo como checkeado */
				"director1", "incident2", null
			}, {
				/* - 2. Un usuario no identificado marca un incident como checkeado */
				null, "incident2", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es director marca un incident como checkeado */
				"visitor1", "incident2", IllegalArgumentException.class
			}, {
				/* - 4. Un director marca un incident null como checkeado */
				"director1", null, IllegalArgumentException.class
			}, {
				/* - 5. Un director marca un incident que no es suyo como checkeado */
				"director1", "incident15", IllegalArgumentException.class
			}, {
				/* - 6. Un director marca un incident que ya estaba checkeado como checkeado */
				"director1", "incident1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			Incident incident = null;
			if (testingData[i][1] != null)
				incident = this.incidentService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			this.templateCheckIncident((String) testingData[i][0], incident, (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateCheckIncident(final String username, final Incident incident, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {

			/* 3. mark an incident as checked */
			this.incidentService.check(incident);
			this.incidentService.flush();

			final Incident incident2 = this.incidentService.findOne(incident.getId());
			Assert.isTrue(incident2.getIsChecked() == true);

			/* 2. list my incidents */

			final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
			final Collection<Incident> myIncidents = this.incidentService.getByDirector(director);
			Assert.isTrue(myIncidents.contains(incident));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-025] - Director delete an Incident
	 * v1.0 - josembell
	 */
	@Test
	public void driverDeleteIncident() {

		final Object testingData[][] = {
			{
				/* + 1. Un director elimina un indicent */
				"director1", "incident2", null
			}, {
				/* - 2. Un usuario no identificado elimina un incident */
				null, "incident2", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es director elimina un incident */
				"visitor1", "incident2", IllegalArgumentException.class
			}, {
				/* - 4. Un director elimina un incident null */
				"director1", null, IllegalArgumentException.class
			}, {
				/* - 5. Un director elimina un incident que no es suyo */
				"director1", "incident15", IllegalArgumentException.class
			}, {
				/* - 6. Un director elimina un incident que ya estaba checkeado como checkeado */
				"director1", "incident1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			Incident incident = null;
			if (testingData[i][1] != null)
				incident = this.incidentService.findOne(this.getEntityId((String) testingData[i][1]));
			this.startTransaction();
			this.templateDeleteIncident((String) testingData[i][0], incident, (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateDeleteIncident(final String username, final Incident incident, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 3. mark an incident as checked */
			this.incidentService.delete(incident);
			this.incidentService.flush();

			/* 2. list my incidents */

			final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
			final Collection<Incident> myIncidents = this.incidentService.getByDirector(director);
			Assert.isTrue(!myIncidents.contains(incident));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

}
