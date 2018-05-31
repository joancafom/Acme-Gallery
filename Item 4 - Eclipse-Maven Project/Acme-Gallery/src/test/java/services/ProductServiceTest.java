
package services;

import java.util.ArrayList;
import java.util.Arrays;
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

import utilities.AbstractTest;
import domain.Product;
import domain.Store;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProductServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private ProductService	productService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private StoreService	storeService;


	// -------------------------------------------------------------------------------
	// [UC-028] Director create a product.
	// 
	// Related requirements:
	//   � REQ 6: A store is composed of products, which are managed by the director of 
	//			  the museum. For each product, the system must keep track of a name, a
	//			  description, a set of pictures, a price and a barcode which must follow
	//			  the standard EAN13.
	//
	//   � REQ 23.23: An actor who is authenticated as an director must be able to
	//                List the museums that she or he manages
	//
	//   � REQ 23.23: An actor who is authenticated as an director must be able to
	//                Create a product for the gift store of a museum that she or he manages. 
	//				  Products cannot be edited.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverCreateProduct() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the name of the product.
		// testingData[i][2] -> the description of the product.
		// testingData[i][3] -> the pictures of the product.
		// testingData[i][4] -> the price of the product.
		// testingData[i][5] -> the barcode of the product.
		// testingData[i][6] -> the store where to create the product
		// testingData[i][7] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A director successfully creates a new product for her or his store.
				"director1", "Test Product", "Test Description", "http://www.tinyurl.com/kittenDP, http://www.tinyurl.com/puppyDP", 54.40, "1111111111116", "store1", null
			}, {
				// - 2) A director tries to create a new product for her or his store (XSS name).
				"director2", "<script>alert('Hacked!');</script>", "Test Description", "http://www.tinyurl.com/kittenDP", 0.01, "1111111111116", "store7", ConstraintViolationException.class
			}, {
				// - 3) A director tries to create a new product for her or his store (One wrong URL).
				"director4", "Test Product", "Test Description", "http://www.tinyurl.com/kittenDP, invalidURL, http://www.tinyurl.com/puppyDP", 54.40, "1111111111116", "store8", IllegalArgumentException.class
			}, {
				// - 4) A director tries to create a new product for her or his store (wrong price).
				"director1", "Test Product", "Test Description", "http://www.tinyurl.com/kittenDP, http://www.tinyurl.com/puppyDP", -0.01, "1111111111116", "store1", ConstraintViolationException.class
			}, {
				// - 5) A director tries to create a new product for her or his store (wrong EAN).
				"director2", "Test Product", "Test Description", "http://www.tinyurl.com/kittenDP, http://www.tinyurl.com/puppyDP", 54.40, "1111111111112", "store7", IllegalArgumentException.class
			}, {
				// - 6) A director tries to create a new product for another's director store.
				"director1", "Test Product", "Test Description", "http://www.tinyurl.com/kittenDP, http://www.tinyurl.com/puppyDP", 54.40, "1111111111116", "store7", IllegalArgumentException.class
			}
		};

		Collection<String> urls;
		Store store;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][3] != null) {
				urls = new ArrayList<String>();
				urls.addAll(Arrays.asList(new String((String) testingData[i][3]).split(",")));
			} else
				urls = null;

			if (testingData[i][6] != null)
				store = this.storeService.findOne(this.getEntityId((String) testingData[i][6]));
			else
				store = null;

			this.startTransaction();

			this.templateCreateProduct((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], urls, (Double) testingData[i][4], (String) testingData[i][5], store, (Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateCreateProduct(final String performer, final String name, final String description, final Collection<String> pictures, final Double price, final String barcode, final Store store, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			// 1. Create a new Product
			final Product product = this.productService.create(store);

			product.setName(name);
			product.setDescription(description);
			product.setPictures(pictures);
			product.setPrice(price);
			product.setBarcode(barcode);
			product.setStore(store);

			final Product savedProduct = this.productService.save(product);
			this.productService.flush();
			this.storeService.flush();

			//Assert that now the stores contains the product
			final Store storeAfter = this.storeService.findOne(store.getId());
			Assert.isTrue(storeAfter.getProducts().contains(savedProduct));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-029] Director delete a product.
	// 
	// Related requirements:
	//
	//   � REQ 23.29: An actor who is authenticated as an director must be able to
	//                Delete a product for the gift store of a museum that she or he
	//				  manages.
	//
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by JA
	// -------------------------------------------------------------------------------

	@Test
	public void driverDeleteProduct() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the product to delete.
		// testingData[i][2] -> the expected exception.

		final Object testingData[][] = {
			{
				// + 1) A director successfully deletes product for her or his store.
				"director1", "product1", null
			}, {
				// - 2) A director tries to delete a product of another director's store.
				"director1", "product10", IllegalArgumentException.class
			}, {
				// - 3) A director tries to delete a null product.
				"director1", null, IllegalArgumentException.class
			}, {
				// - 4) A sponsor tries to delete a null product.
				"sponsor1", "product1", IllegalArgumentException.class
			}, {
				// - 5) A director tries to delete a product with a null store.
				"director1", "product2", IllegalArgumentException.class
			}
		};

		Product product;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				product = this.productService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				product = null;

			if (i == testingData.length - 1)
				product.setStore(null);

			this.startTransaction();

			this.templateDeleteProduct((String) testingData[i][0], product, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateDeleteProduct(final String performer, final Product product, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			Store productStore = null;

			if (product != null)
				productStore = product.getStore();

			// 1. Delete the product
			this.productService.delete(product);
			this.productService.flush();

			//2. Get the list of products of its store and Assert it does not longer appear
			final Store storeAfter = this.storeService.findOne(productStore.getId());

			Assert.isTrue(!storeAfter.getProducts().contains(product));

			//3. Furthermore, the product must not be in the system
			Assert.isNull(this.productService.findOne(product.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
