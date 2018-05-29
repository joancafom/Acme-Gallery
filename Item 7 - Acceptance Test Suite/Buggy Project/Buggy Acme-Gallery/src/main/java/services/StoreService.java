
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.StoreRepository;
import security.LoginService;
import domain.Director;
import domain.Museum;
import domain.Product;
import domain.Store;

@Service
@Transactional
public class StoreService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private StoreRepository	storeRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private DirectorService	directorService;

	@Autowired
	private Validator		validator;

	@Autowired
	private MuseumService	museumService;

	@Autowired
	private ProductService	productService;


	// CRUD Methods -----------------------------------------------------------------------------------

	/* v1.0 - josembell */
	public Store findOne(final int storeId) {
		return this.storeRepository.findOne(storeId);
	}

	/* v1.0 - josembell */
	public Store create(final Museum museum) {
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(museum);
		Assert.notNull(director);
		Assert.isTrue(museum.getStore() == null);

		final Store store = new Store();
		store.setMuseum(museum);
		store.setProducts(new HashSet<Product>());

		return store;
	}

	/* v1.0 - josembell */
	public Store save(final Store store) {
		Assert.notNull(store);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(store.getMuseum().getDirector().equals(director));

		final Museum museum = store.getMuseum();
		store.setMuseum(null);
		final Store saved = this.storeRepository.save(store);
		saved.setMuseum(museum);
		final Store res = this.storeRepository.save(saved);
		res.getMuseum().setStore(res);
		this.museumService.save(res.getMuseum());

		return res;

	}

	/* v1.0 - josembell */
	public void delete(final Store store) {
		Assert.notNull(store);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(store.getMuseum().getDirector().equals(director));

		final Collection<Product> products = new HashSet<Product>(store.getProducts());
		for (final Product p : products)
			this.productService.delete(p);

		store.getMuseum().setStore(null);
		this.museumService.save(store.getMuseum());

		this.storeRepository.delete(store);
	}

	/* v1.0 - josembell */
	public Store reconstruct(final Store prunedStore, final BindingResult binding) {

		Assert.notNull(prunedStore);

		final Store store;

		if (prunedStore.getId() == 0) {
			store = this.create(prunedStore.getMuseum());
			store.setEmail(prunedStore.getEmail());
			store.setLogo(prunedStore.getLogo());
			store.setMuseum(prunedStore.getMuseum());
			store.setName(prunedStore.getName());
			store.setPhoneNumber(prunedStore.getPhoneNumber());
			store.setProducts(new HashSet<Product>());

		} else {
			final Store retrievedStore = this.findOne(prunedStore.getId());
			Assert.notNull(retrievedStore);

			store = prunedStore;

			store.setProducts(retrievedStore.getProducts());
		}

		this.validator.validate(store, binding);

		return store;
	}
	//Other Business Methods --------------------------------------------------------------------------

}
