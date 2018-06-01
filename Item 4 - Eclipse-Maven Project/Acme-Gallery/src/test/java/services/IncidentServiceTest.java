
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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


	/*
	 * [UC-024] - Director check an Incident
	 * 1. Log in as director
	 * 2. List my incidents
	 * 3. Mark an incident as checked
	 * 
	 * REQ: 19, 29.11
	 * 
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
	 * 1. Log in as director
	 * 2. List my incidents
	 * 3. Delete an incident
	 * 
	 * REQ: 19, 29.11
	 * 
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
