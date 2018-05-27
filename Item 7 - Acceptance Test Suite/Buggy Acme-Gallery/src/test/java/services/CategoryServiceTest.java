
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

import utilities.AbstractTest;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private CategoryService	categoryService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// [UC-006] Administrator create Category --------
	// v1.0 - Alicia

	@Test
	public void driverCreateCategory() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> name of the Category.
		// testingData[i][2] -> parentCategory of the Category.
		// testingData[i][3] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) An Admin correctly creates a Category.
				"admin", "testCategory", "category1", null
			}, {
				// 2 - (-) An Admin creates a Category with the same name in the same context.
				"admin", "Paintings", "category1", IllegalArgumentException.class
			}, {
				// 3 - (-) An Admin creates a Category with the same name as the root category.
				"admin", "CATEGORY", "category1", IllegalArgumentException.class
			}, {
				// 4 - (-) An Admin creates a Category with no parent category.
				"admin", "testCategory", null, IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor tries to create a Category.
				"visitor1", "testCategory", "category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Category parentCategory = null;

			if ((String) testingData[i][2] != null)
				parentCategory = this.categoryService.findOne(super.getEntityId((String) testingData[i][2]));

			this.startTransaction();

			this.templateCreateCategory((String) testingData[i][0], (String) testingData[i][1], parentCategory, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateCreateCategory(final String username, final String name, final Category parentCategory, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Create a Category

			final Category createdCategory = this.categoryService.create(parentCategory);

			createdCategory.setName(name);
			createdCategory.setParentCategory(parentCategory);

			final Category savedCategory = this.categoryService.saveCreate(createdCategory);

			// Flush
			this.categoryService.flush();

			// 3. List the Categories

			final Collection<Category> allCategories = this.categoryService.findAll();
			Assert.isTrue(allCategories.contains(savedCategory));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// [UC-007] Administrator delete Category --------
	// v1.0 - Alicia

	@Test
	public void driverDeleteCategory() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> Category to remove.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) An Administrator correctly removes a Category.
				"admin", "category20", null
			}, {
				// 2 - (-) An Administrator removes a non-existing Category.
				"admin", "abcdefgh", IllegalArgumentException.class
			}, {
				// 3 - (-) An Administrator removes a null Category.
				"admin", null, IllegalArgumentException.class
			}, {
				// 4 - (-) An Administrator removes the root Category.
				"admin", "category1", IllegalArgumentException.class
			}, {
				// 5 - (-) A Visitor removes a Category.
				"visitor1", "category20", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Category category = null;

			if (i == 0 || i > 2)
				category = this.categoryService.findOne(super.getEntityId((String) testingData[i][1]));

			if (i == 1)
				category = new Category();

			this.startTransaction();

			this.templateDeleteCategory((String) testingData[i][0], category, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateDeleteCategory(final String username, final Category category, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List Categories
			final Collection<Category> oldCategories = this.categoryService.findAll();

			// 3. Remove a Category
			this.categoryService.delete(category);

			// Flush
			this.categoryService.flush();

			// 4. List Categories again and check
			final Collection<Category> newCategories = this.categoryService.findAll();

			Assert.isTrue(oldCategories.contains(category));
			Assert.isTrue(!newCategories.contains(category));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
