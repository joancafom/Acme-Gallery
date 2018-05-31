
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;

import utilities.AbstractTest;
import domain.Category;
import domain.Exhibition;
import domain.Guide;
import domain.Room;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ExhibitionServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private ExhibitionService	exhibitionService;

	//Helping Services

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private RoomService			roomService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private GuideService		guideService;


	/*
	 * [UC-002] - Search for exhibitions
	 * 1. Log in to the system
	 * 2. Get to the exhibition browser
	 * 3. List your search results
	 * 4. Display an exhibition
	 * 
	 * v1.1 - josembell
	 */
	@Test
	public void driverSearchExhibitions() {

		final Object testingData[][] = {
			{
				// 1 - (+) Un usuario no identificado busca exhibiciones
				null, "Van Gogh", "exhibition1", null
			}, {
				// 2 - (+) Un visitor busca exhibiciones
				"visitor1", "Van Gogh", "exhibition1", null
			}, {
				// 3 - (+) Un administrador busca exhibiciones
				"admin", "Van Gogh", "exhibition1", null
			}, {
				// 4 - (+) Un sponsor busca exhibiciones
				"sponsor1", "Van Gogh", "exhibition1", null
			}, {
				// 5 - (+) Un director busca exhibiciones
				"director1", "Van Gogh", "exhibition1", null
			}, {
				// 6 - (+) Un guia busca exhibiciones
				"guide1", "Van Gogh", "exhibition1", null
			}, {
				// 7 - (+) Un critic busca exhibiciones
				"critic1", "Van Gogh", "exhibition1", null
			}, {
				/* 8 - (-) Un user busca exhibiciones con keyword null */
				null, null, "exhibition1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Exhibition exhibition = null;
			if (testingData[i][2] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][2]));
			this.startTransaction();

			this.templateSearchExhibitions((String) testingData[i][0], (String) testingData[i][1], exhibition, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	private void templateSearchExhibitions(final String username, final String keyword, final Exhibition exhibition, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Authenticate */
		this.authenticate(username);

		try {
			/* 2. Search by keyword */
			final Collection<Exhibition> searchResults = this.exhibitionService.getByKeyword(keyword);

			/* 3. Display -> entra por parametros */
			Integer exId = null;
			if (exhibition != null)
				exId = exhibition.getId();

			final Exhibition exDisplay = this.exhibitionService.findOne(exId);
			Assert.notNull(searchResults.contains(exDisplay));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	// -------------------------------------------------------------------------------
	// [UC-020] Director creates an Exhibition.
	// 
	// Related requirements:
	//   � REQ 8: Directors organize exhibitions. For every exhibition, the system must store 
	//			  a ticker (which must follow the pattern: uuuu-XXXX where "uuuu" is the username
	//			  of the actor creating the exhibition and "XXXX" is a string chosen by the
	//			  director. Both have a variable size), a title, the room where is going to take 
	//			  place, a description, a starting date, an ending date and a collection of links 
	//			  to external webpages that provide further information about the author.
	//
	//   � REQ 9: Exhibitions can be either public or private. A day pass must be purchased in order 
	//			  to access private exhibitions whereas public ones can be visited by all the visitors
	//			  of a museum
	//
	//   � REQ 10: An exhibition belongs to a category. For each category, the system must store a name,
	//			   which must be unique within the context of the same parent category; that is, several
	//			   categories may have the same name as long as they do not have the same parent. Categories
	//			   are organized into a tree whose root is a fictitious category called "CATEGORY".
	//
	//   � REQ 23.4: Create an exhibition for her museums. An exhibition can take place in a room as long 
	//				 as there is no exhibition taking place there in the selected dates
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverExhibitionCreate() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the ticker of the exhibition.
		// testingData[i][2] -> the title of the exhibition.
		// testingData[i][3] -> the description of the exhibition.
		// testingData[i][4] -> the startingDate of the exhibition.
		// testingData[i][5] -> the endingDate of the exhibition.
		// testingData[i][6] -> the websites of the exhibition.
		// testingData[i][7] -> if the Exhibition is private or not
		// testingData[i][8] -> the price of the exhibition.
		// testingData[i][9] -> the category of the exhibition.
		// testingData[i][10] -> the room of the exhibition.
		// testingData[i][11] -> the expected exception.

		final LocalDate tomorrow = new LocalDate().plusDays(1);
		final LocalDate nextYear = tomorrow.plusYears(1);

		final Object testingData[][] = {
			{
				// + 1) A director successfully creates a new exhibition in a room with no other exhibition in that period.
				"director1", "test1", "Test Title", "Test Description", this.formatDate("18-08-2018 21:00"), this.formatDate("20-08-2018 19:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", null
			},
			{
				// - 2) A director tries to create a new exhibition that begins at the same time other one finishes in the same room
				"director1", "test2", "Test Title", "Test Description", this.getExhibitionEndingDate("exhibition5"), nextYear.toDate(), "http://www.apple.com, http://www.google.es", false, 0.0, "category1", this.getExhibitionRoom("exhibition5").getId(),
				IllegalArgumentException.class
			}, {
				// - 3) A director tries to create a new exhibition in a room of another director's museum
				"director2", "test3", "Test Title", "Test Description", tomorrow.toDate(), nextYear.toDate(), "http://www.apple.com, http://www.google.es", false, 0.0, "category1", "room14", IllegalArgumentException.class
			}, {
				// - 4) A director tries to create an exhibition with a null room
				"director1", "test4", "Test Title", "Test Description", tomorrow.toDate(), nextYear.toDate(), "http://www.apple.com, http://www.google.es", false, 0.0, "category1", null, IllegalArgumentException.class
			}, {
				// - 5) A director tries to create an public exhibition with price > 0
				"director1", "test5", "Test Title", "Test Description", tomorrow.toDate(), nextYear.toDate(), "http://www.apple.com, http://www.google.es", false, 0.01, "category1", "room14", IllegalArgumentException.class
			}
		};

		Category category;
		Room room;
		Collection<String> websites;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][9] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][9]));
			else
				category = null;

			if (testingData[i][10] != null) {
				if (i != 1)
					room = this.roomService.findOne(this.getEntityId((String) testingData[i][10]));
				else
					room = this.roomService.findOne((Integer) testingData[i][10]);
			} else
				room = null;

			if (testingData[i][6] != null) {
				websites = new ArrayList<String>();
				websites.addAll(Arrays.asList(new String((String) testingData[i][6]).split(",")));
			} else
				websites = null;

			this.startTransaction();

			this.templateExhibitionCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], websites, (Boolean) testingData[i][7],
				(Double) testingData[i][8], category, room, (Class<?>) testingData[i][11]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateExhibitionCreate(final String performer, final String ticker, final String title, final String description, final Date startingDate, final Date endingDate, final Collection<String> websites, final Boolean isPrivate,
		final Double price, final Category category, final Room room, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Create a new Exhibition
			final Exhibition exhibition = this.exhibitionService.create();

			exhibition.setTicker(ticker);
			exhibition.setTitle(title);
			exhibition.setDescription(description);
			exhibition.setStartingDate(startingDate);
			exhibition.setEndingDate(endingDate);
			exhibition.setWebsites(websites);
			exhibition.setIsPrivate(isPrivate);
			exhibition.setPrice(price);
			exhibition.setCategory(category);
			exhibition.setRoom(room);

			final Exhibition savedExhibiton = this.exhibitionService.saveCreateAndEdit(exhibition);
			this.exhibitionService.flush();

			//Assert that the system now has a record matching the created Exhibition
			Assert.notNull(this.exhibitionService.findOne(savedExhibiton.getId()));

			//Assert that all its collections are initialized as empty
			Assert.isTrue(exhibition.getCritiques().isEmpty());
			Assert.isTrue(exhibition.getSponsorships().isEmpty());
			Assert.isTrue(exhibition.getDayPasses().isEmpty());
			Assert.isTrue(exhibition.getGuides().isEmpty());
			Assert.isTrue(exhibition.getArtworks().isEmpty());

			//Assert that the Museum contains the recently added one among its exhibitions
			Assert.isTrue(this.exhibitionService.getAllByMuseum(exhibition.getRoom().getMuseum()).contains(savedExhibiton));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * [UC-022] - Director add Guide
	 * 1. Log in as a director
	 * 2. List my exhibitions
	 * 3. Add guide to a exhibition
	 * 
	 * REQ:
	 * 
	 * v1.0 - josembell
	 */
	@Test
	public void driverAddGuide() {

		final Object testingData[][] = {
			{
				/* + 1. Un director a�ade un guia perteneciente a un museo a una de sus exhibiciones */
				"director1", "exhibition2", "guide1", null
			}, {
				/* - 2. Un usuario no identificado a�ade un guia */
				null, "exhibition2", "guide1", IllegalArgumentException.class
			}, {
				/* - 3. Un usuario que no es un director a�ade un guia */
				"visitor1", "exhibition2", "guide1", IllegalArgumentException.class
			}, {
				/* - 4. Un director a�ade un guia que no le pertenece al museo */
				"director5", "exhibition13", "guide1", IllegalArgumentException.class
			}, {
				/* - 5. Un director a�ade un guia a una exhibicion que no le pertenece */
				"director1", "exhibition13", "guide1", IllegalArgumentException.class
			}, {
				/* - 6. Un director a�ade un guia null a una exhibicion */
				"director1", "exhibition2", null, IllegalArgumentException.class
			}, {
				/* - 7. Un director a�ade un guia a una exhibicion null */
				"director1", null, "guide1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Exhibition exhibition = null;
			Guide guide = null;
			if (testingData[i][1] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][1]));
			if (testingData[i][2] != null)
				guide = this.guideService.findOne(this.getEntityId((String) testingData[i][2]));
			this.startTransaction();
			System.out.println(i);
			this.templateAddGuide((String) testingData[i][0], exhibition, guide, (Class<?>) testingData[i][3]);
			System.out.println(i);
			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	/* v1.0 - josembell */
	protected void templateAddGuide(final String username, final Exhibition exhibition, final Guide guide, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. authenticate */
		this.authenticate(username);

		try {
			/* 2. list my exhibitions */
			final Collection<Exhibition> myExhibitions = this.exhibitionService.getExhibitionsByPrincipal();

			/* 3. add the guide */

			guide.getExhibitions().add(exhibition);
			this.guideService.save(guide);
			exhibition.getGuides().add(guide);

			final Exhibition saved = this.exhibitionService.saveCreateAndEdit(exhibition);
			this.exhibitionService.flush();

			/* 4. check that the exhibition is in the list */
			Assert.isTrue(myExhibitions.contains(exhibition));
			Assert.isTrue(saved.getGuides().contains(guide));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-021] Director edit an exhibition. *EXTENDED*
	// 
	// Related requirements:
	//   � REQ 8: Directors organize exhibitions. For every exhibition, the system must store 
	//			  a ticker (which must follow the pattern: uuuu-XXXX where "uuuu" is the username
	//			  of the actor creating the exhibition and "XXXX" is a string chosen by the
	//			  director. Both have a variable size), a title, the room where is going to take 
	//			  place, a description, a starting date, an ending date and a collection of links 
	//			  to external webpages that provide further information about the author.
	//
	//   � REQ 9: Exhibitions can be either public or private. A day pass must be purchased in order 
	//			  to access private exhibitions whereas public ones can be visited by all the visitors
	//			  of a museum
	//
	//   � REQ 10: An exhibition belongs to a category. For each category, the system must store a name,
	//			   which must be unique within the context of the same parent category; that is, several
	//			   categories may have the same name as long as they do not have the same parent. Categories
	//			   are organized into a tree whose root is a fictitious category called "CATEGORY".
	//
	//   � REQ 23.5: Edit the details of an exhibition she has created as long as it hasn't started. In case 
	//				 it is a private exhibition that has already sold day passes, she is only able to 
	//				 edit: title, description, websites and category. The ticker can never be edited.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverExhibitionEdit() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the ticker of the exhibition.
		// testingData[i][2] -> the title of the exhibition.
		// testingData[i][3] -> the description of the exhibition.
		// testingData[i][4] -> the startingDate of the exhibition.
		// testingData[i][5] -> the endingDate of the exhibition.
		// testingData[i][6] -> the websites of the exhibition.
		// testingData[i][7] -> if the Exhibition is private or not
		// testingData[i][8] -> the price of the exhibition.
		// testingData[i][9] -> the category of the exhibition.
		// testingData[i][10] -> the room of the exhibition.
		// testingData[i][11] -> if we want to change the ticker or not.
		// testingData[i][12] -> if we only want to update the fields a 
		//						 private exhibition with sold passes is allowed to.
		// testingData[i][13] -> the exhibition to update.
		// testingData[i][14] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A director successfully edits a public exhibition that has not started yet to a room with no other exhibition in that period.
				"director1", "test1", "Test Title", "Test Description", this.formatDate("18-08-2018 21:00"), this.formatDate("20-08-2018 19:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", false, false, "exhibition4",
				null
			},
			{
				// + 2) A director successfully edits a private exhibition that has not started yet and has not sold any day passes
				"director5", "test2", "Test Title", "Test Description", this.formatDate("18-08-2028 21:00"), this.formatDate("20-08-2028 19:00"), "http://www.apple.com, http://www.google.es", false, 0.0, "category2", "room18", false, false,
				"exhibition13", null
			},
			{
				// + 3) A director successfully edits a private exhibition that has not started yet and has sold some day passes
				"director6", "test3", "Test Title", "Test Description", this.formatDate("18-08-2028 21:00"), this.formatDate("20-08-2028 19:00"), "http://www.apple.com, http://www.google.es", false, 0.0, "category2", "room18", false, true, "exhibition14",
				null
			},
			{
				// - 4) A director tries to edit forbidden fields of a private exhibition that has not started yet but has sold some day passes
				"director6", "test3", "Test Title", "Test Description", this.formatDate("18-08-2028 21:00"), this.formatDate("20-08-2028 19:00"), "http://www.apple.com, http://www.google.es", false, 0.0, "category2", "room18", false, false,
				"exhibition14", null
			},
			{
				// - 5) A director tries to edit a public exhibition that has already started.
				"director1", "test1", "Test Title", "Test Description", this.formatDate("18-08-2018 21:00"), this.formatDate("20-08-2018 19:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", false, false, "exhibition1",
				IllegalArgumentException.class
			},
			{
				// - 5) A director tries to edit a private exhibition that has not sold any day passes with XSS in its title.
				"director5", "test2", "Test <script>alert('hacked!');</script> Title", "Test Description", this.formatDate("18-08-2028 21:00"), this.formatDate("20-08-2028 19:00"), "http://www.apple.com, http://www.google.es", false, 0.0, "category2",
				"room18", false, false, "exhibition13", ConstraintViolationException.class
			},
			{
				// - 6) A director tries to edit a fellow director's public exhibition that has not started
				"director2", "test1", "Test Title", "Test Description", this.formatDate("18-08-2018 21:00"), this.formatDate("20-08-2018 19:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", false, false, "exhibition4",
				IllegalArgumentException.class
			},
			{
				// - 7) A visitor tries to edit a private exhibition that has not started yet and has sold some day passes
				"visitor1", "test1", "Test Title", "Test Description", this.formatDate("18-08-2018 21:00"), this.formatDate("20-08-2018 19:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", false, false, "exhibition14",
				IllegalArgumentException.class
			},
			{
				// - 8) A director tries to edit a public exhi8bition that has not started yet with wrong dates (ending before starting)
				"director1", "test1", "Test Title", "Test Description", this.formatDate("20-08-2018 19:00"), this.formatDate("18-08-2018 21:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", false, false, "exhibition4",
				IllegalArgumentException.class
			},
			{
				// - 9) A director tries to edit a public exhibition that has not started yet to a room in which other exhibition takes place in those dates
				"director1", "test1", "Test Title", "Test Description", this.formatDate("18-08-2018 21:00"), this.formatDate("20-08-2018 20:00"), "http://www.apple.com, http://www.google.es", true, 12.0, "category1", "room3", false, false, "exhibition4",
				IllegalArgumentException.class
			},
			{
				// - 10) A director tries to edit a private exhibition that has not started yet and has not sold any day passes by providing a null starting date
				"director5", "test2", "Test Title", "Test Description", null, this.formatDate("20-08-2028 19:00"), "http://www.apple.com, http://www.google.es", false, 0.0, "category2", "room18", false, false, "exhibition13",
				IllegalArgumentException.class
			}
		};

		Category category;
		Room room;
		Collection<String> websites;
		String ticker;
		Exhibition exhibition;

		for (int i = 0; i < testingData.length; i++) {

			System.err.println(i);

			//Makes a collection of Websites out of the provided String
			if (testingData[i][6] != null) {
				websites = new ArrayList<String>();
				websites.addAll(Arrays.asList(new String((String) testingData[i][6]).split(",")));
			} else
				websites = null;

			//Retrieves the Category out of the provided String
			if (testingData[i][9] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][9]));
			else
				category = null;

			//Retrieves the Room out of the provided String
			if (testingData[i][10] != null)
				room = this.roomService.findOne(this.getEntityId((String) testingData[i][10]));
			else
				room = null;

			//Retrieves the Exhibition to update out of the provided String
			if (testingData[i][13] != null)
				exhibition = this.exhibitionService.findOne(this.getEntityId((String) testingData[i][13]));
			else
				exhibition = null;

			//Selects the value applied for the ticker in the testcase (if we want to change it or not)
			if ((Boolean) testingData[i][11])
				ticker = (String) testingData[i][1];
			else if (exhibition != null)
				ticker = exhibition.getTicker();
			else
				ticker = null;

			this.startTransaction();

			this.templateExhibitionEdit((String) testingData[i][0], exhibition, (Boolean) testingData[i][12], ticker, (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], websites,
				(Boolean) testingData[i][7], (Double) testingData[i][8], category, room, (Class<?>) testingData[i][14]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateExhibitionEdit(final String performer, final Exhibition originalExhibition, final Boolean onlyUpdatePP, final String ticker, final String title, final String description, final Date startingDate, final Date endingDate,
		final Collection<String> websites, final Boolean isPrivate, final Double price, final Category category, final Room room, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			final Exhibition exhibitionToUpdate = this.getCopy(originalExhibition);

			// 1. Edit the exhibiton that is given via parameters
			exhibitionToUpdate.setTitle(title);
			exhibitionToUpdate.setDescription(description);
			exhibitionToUpdate.setWebsites(websites);
			exhibitionToUpdate.setCategory(category);
			exhibitionToUpdate.setTicker(ticker);

			//If we don't want to update only the fields allowed in 
			//the case it were a private exhibition with sold passes. (Req 23.5)
			if (!onlyUpdatePP) {
				exhibitionToUpdate.setStartingDate(startingDate);
				exhibitionToUpdate.setEndingDate(endingDate);
				exhibitionToUpdate.setPrice(price);
				exhibitionToUpdate.setRoom(room);
				exhibitionToUpdate.setIsPrivate(isPrivate);
			}

			//The exhibition to be updated is passed through a reconstruct as in real life
			final Exhibition reconstructedExhibition = this.exhibitionService.reconstructSave(exhibitionToUpdate, new BeanPropertyBindingResult(exhibitionToUpdate, ""));

			final Exhibition updatedExhibition = this.exhibitionService.saveCreateAndEdit(reconstructedExhibition);
			this.exhibitionService.flush();

			//Assert that the updated exhibition appears in the listing of exhibitions of its director
			Exhibition foundExhibition = null;

			for (final Exhibition e : this.exhibitionService.getByDirector(updatedExhibition.getRoom().getMuseum().getDirector()))
				if (e.equals(updatedExhibition)) {
					foundExhibition = e;
					break;
				}

			Assert.notNull(foundExhibition);

			//Assert that the fields were updated or remained the same
			Assert.isTrue(foundExhibition.getTitle().equals(title));
			Assert.isTrue(foundExhibition.getDescription().equals(description));
			Assert.isTrue(foundExhibition.getWebsites().containsAll(websites));
			Assert.isTrue(foundExhibition.getWebsites().size() == websites.size());
			Assert.isTrue(foundExhibition.getCategory().equals(category));
			Assert.isTrue(foundExhibition.getTicker().equals(originalExhibition.getTicker()));

			//Check that, in case of a private exhibition with some day passes sold, the fields
			//you were not allowed to remained the same
			if (onlyUpdatePP) {
				Assert.isTrue(foundExhibition.getStartingDate().equals(originalExhibition.getStartingDate()));
				Assert.isTrue(foundExhibition.getEndingDate().equals(originalExhibition.getEndingDate()));
				Assert.isTrue(foundExhibition.getPrice().equals(originalExhibition.getPrice()));
				Assert.isTrue(foundExhibition.getRoom().equals(originalExhibition.getRoom()));
				Assert.isTrue(foundExhibition.getIsPrivate() == originalExhibition.getIsPrivate());
			} else {
				System.out.println(foundExhibition.getStartingDate().compareTo(startingDate));
				Assert.isTrue(foundExhibition.getStartingDate().compareTo(startingDate) == 0);
				Assert.isTrue(foundExhibition.getEndingDate().compareTo(endingDate) == 0);
				Assert.isTrue(foundExhibition.getPrice().equals(price));
				Assert.isTrue(foundExhibition.getRoom().equals(room));
				Assert.isTrue(foundExhibition.getIsPrivate() == isPrivate);
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
	//Auxiliary Methods
	private Date formatDate(final String date) {
		Date res;

		try {
			res = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
		} catch (final ParseException e) {
			res = null;
		}

		return res;
	}

	private Date getExhibitionEndingDate(final String beanName) {

		final Exhibition exh = this.exhibitionService.findOne(this.getEntityId(beanName));

		if (exh == null)
			return null;
		else
			return exh.getEndingDate();

	}

	private Room getExhibitionRoom(final String beanName) {

		final Exhibition exh = this.exhibitionService.findOne(this.getEntityId(beanName));

		if (exh == null)
			return null;
		else
			return exh.getRoom();

	}

	private Exhibition getCopy(final Exhibition original) {

		final Exhibition res = new Exhibition();

		res.setArtworks(original.getArtworks());
		res.setCategory(original.getCategory());
		res.setCritiques(original.getCritiques());
		res.setDayPasses(original.getDayPasses());
		res.setDescription(original.getDescription());
		res.setEndingDate(original.getEndingDate());
		res.setGuides(original.getGuides());
		res.setId(original.getId());
		res.setIsPrivate(original.getIsPrivate());
		res.setPrice(original.getPrice());
		res.setRoom(original.getRoom());
		res.setSponsorships(original.getSponsorships());
		res.setStartingDate(original.getStartingDate());
		res.setTicker(original.getTicker());
		res.setTitle(original.getTitle());
		res.setVersion(original.getVersion());
		res.setWebsites(original.getWebsites());

		return res;
	}
}
