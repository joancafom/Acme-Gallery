
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProductRepository;
import security.LoginService;
import domain.Director;
import domain.Product;
import domain.Store;

@Service
@Transactional
public class ProductService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ProductRepository	productRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService		directorService;


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

	//Other Business Methods --------------------------------------------------------------------------

	public Page<Product> findAllPaginatedByStore(final Integer page, final int size, final Store store) {
		final Page<Product> res = this.productRepository.findAllByStore(store.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

}
