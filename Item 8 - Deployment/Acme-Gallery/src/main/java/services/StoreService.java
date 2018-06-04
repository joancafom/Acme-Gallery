
package services;

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

	// v1.0 - Alicia
	public Store saveSimple(final Store store) {
		Assert.notNull(store);

		return this.storeRepository.save(store);
	}

	/* v1.0 - josembell */
	// v2.0 - Alicia
	public Store save(final Store store) {
		Assert.notNull(store);
		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.notNull(store.getMuseum());
		Assert.isTrue(store.getMuseum().getDirector().equals(director));

		if (store.getId() != 0) {
			final Store oldStore = this.findOne(store.getId());
			Assert.isTrue(oldStore.getMuseum().getDirector().equals(director));
		}

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
		Assert.isTrue(this.storeRepository.exists(store.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(store.getMuseum().getDirector().equals(director));

		//Orphan removal takes care of removing the products

		final Museum museum = this.museumService.findOne(store.getMuseum().getId());
		Assert.notNull(museum);

		museum.setStore(null);
		this.museumService.save(museum);
		this.museumService.flush();

		this.storeRepository.delete(store);
	}

	// v1.0 - JA
	public void flush() {

		this.storeRepository.flush();
	}

	//Other Business Methods --------------------------------------------------------------------------

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
			store.setMuseum(retrievedStore.getMuseum());
		}

		this.validator.validate(store, binding);

		return store;
	}

	// v1.0 - Alicia
	public Store getStoreByMuseum(final Museum museum) {
		Assert.notNull(museum);

		return this.storeRepository.getStoreByMuseumId(museum.getId());
	}

}
