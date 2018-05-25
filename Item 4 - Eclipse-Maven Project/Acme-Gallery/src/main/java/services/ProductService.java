
package services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProductRepository;
import security.LoginService;
import domain.Director;
import domain.Product;
import domain.Store;

@Service
@Transactional
public class ProductService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ProductRepository	productRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService		directorService;

	@Autowired
	private Validator			validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	/* v1.0 - josembell */
	public Product findOne(final int productId) {
		return this.productRepository.findOne(productId);
	}

	/* v1.0 - josembell */
	public void delete(final Product product) {
		Assert.notNull(product);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(product.getStore().getMuseum().getDirector().equals(director));

		product.getStore().getProducts().remove(product);
		this.productRepository.delete(product);
	}

	/* v1.0 - josembell */
	public Product create(final Store store) {
		Assert.notNull(store);

		final Product product = new Product();
		product.setPictures(new HashSet<String>());
		product.setStore(store);

		return product;
	}

	/* v1.0 - josembell */
	public Product save(final Product product) {
		Assert.notNull(product);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(product.getStore().getMuseum().getDirector().equals(director));

		Assert.notNull(product.getPictures());
		final Boolean isEAN13 = this.isEAN13(product.getBarcode());
		Assert.isTrue(isEAN13 == true);

		Assert.isTrue(!product.getPictures().isEmpty());
		for (final String s : product.getPictures())
			try {
				@SuppressWarnings("unused")
				final URL url = new java.net.URL(s);
			} catch (final MalformedURLException e) {
				throw new IllegalArgumentException();
			}

		return this.productRepository.save(product);
	}

	//Other Business Methods --------------------------------------------------------------------------

	/* v1.0 - josembell */
	public Page<Product> findAllPaginatedByStore(final Integer page, final int size, final Store store) {
		final Page<Product> res = this.productRepository.findAllByStore(store.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Product reconstruct(final Product prunedProduct, final BindingResult binding) {

		Assert.notNull(prunedProduct);

		final Product product = new Product();

		if (prunedProduct.getId() == 0) {
			product.setBarcode(prunedProduct.getBarcode());
			product.setDescription(prunedProduct.getDescription());
			product.setName(prunedProduct.getName());
			product.setPictures(prunedProduct.getPictures());
			product.setPrice(prunedProduct.getPrice());
			product.setStore(prunedProduct.getStore());
		}

		this.validator.validate(product, binding);

		return product;
	}

	public Boolean isEAN13(final String ean13number) {
		final String firstTwelveDigits = ean13number.substring(0, 12);
		final String checkControl = ean13number.substring(ean13number.length() - 1);

		final char[] charDigits = firstTwelveDigits.toCharArray();
		final int[] ean13 = {
			1, 3
		};
		int sum = 0;
		for (int i = 0; i < charDigits.length; i++)
			sum += Character.getNumericValue(charDigits[i]) * ean13[i % 2];
		int checksum = 10 - sum % 10;

		if (checksum == 10)
			checksum = 0;

		final String checksumString = String.valueOf(checksum);
		if (checksumString.equals(checkControl))
			return true;
		else
			return false;

	}
}
