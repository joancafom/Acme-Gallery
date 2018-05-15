
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ReviewRepository;
import security.LoginService;
import domain.Administrator;
import domain.Museum;
import domain.Review;

@Service
@Transactional
public class ReviewService extends ActorService {

	//Managed Repository
	@Autowired
	private ReviewRepository		reviewRepository;

	//Supporting Services
	@Autowired
	private AdministratorService	adminService;


	//CRUD Methods

	// v1.0 - JA
	public Review save(final Review review) {
		Assert.notNull(review);

		//Beware to change this method! SystemConfigurationService.updateTaboo uses it!
		return this.reviewRepository.save(review);
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Page<Review> findAllByMuseum(final Museum museum, final int page, final int size) {

		//Does not have an equivalent as it is bidirectional
		Assert.notNull(museum);
		return this.reviewRepository.findAllByMuseumId(museum.getId(), new PageRequest(page - 1, size));
	}

	public Collection<Review> findTabooed() {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.reviewRepository.findTabooed();
	}

	public Page<Review> findTabooed(final int page, final int size) {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.reviewRepository.findTabooed(new PageRequest(page - 1, size));
	}

	public Collection<Review> findNotTabooed() {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.reviewRepository.findNotTabooed();
	}

	public Page<Review> findNotTabooed(final int page, final int size) {

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.reviewRepository.findNotTabooed(new PageRequest(page - 1, size));
	}
}
