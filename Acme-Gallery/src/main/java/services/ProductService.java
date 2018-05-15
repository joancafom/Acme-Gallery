
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProductRepository;
import domain.Product;
import domain.Store;

@Service
@Transactional
public class ProductService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ProductRepository	productRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	/* v1.0 - josembell */
	public Product findOne(final int productId) {
		return this.productRepository.findOne(productId);
	}

	//Other Business Methods --------------------------------------------------------------------------

	public Page<Product> findAllPaginatedByStore(final Integer page, final int size, final Store store) {
		final Page<Product> res = this.productRepository.findAllByStore(store.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}
}
