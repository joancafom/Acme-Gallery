
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewRepository;
import security.LoginService;
import domain.Administrator;
import domain.DayPass;
import domain.Museum;
import domain.Review;
import domain.Visitor;

@Service
@Transactional
public class ReviewService {

	//Managed Repository
	@Autowired
	private ReviewRepository			reviewRepository;

	//Supporting Services
	@Autowired
	private AdministratorService		adminService;

	@Autowired
	private VisitorService				visitorService;

	@Autowired
	private DayPassService				dayPassService;

	@Autowired
	private SystemConfigurationService	sysConfigService;

	@Autowired
	private Validator					validator;


	//CRUD Methods

	/* v1.0 - josembell */
	public Review create(final Museum museum) {
		Assert.notNull(museum);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Review review = new Review();
		review.setMuseum(museum);
		review.setVisitor(visitor);
		review.setContainsTaboo(false);

		return review;
	}

	/* v1.0 - josembell */
	public Review saveCreate(final Review review) {
		Assert.notNull(review);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Collection<DayPass> dayPassesByVisitor = this.dayPassService.findAllByMuseumAndPrincipal(review.getMuseum());
		Assert.isTrue(dayPassesByVisitor.isEmpty() == false);

		review.setCreationDate(new Date(System.currentTimeMillis() - 1000));
		final Boolean containsTaboo = this.sysConfigService.containsTaboo(review.getBody());
		review.setContainsTaboo(containsTaboo);

		Assert.isTrue(review.getScore() % 1 == 0);

		final Review saved = this.reviewRepository.save(review);
		visitor.getReviews().add(saved);
		this.visitorService.save(visitor);

		return saved;
	}
	// v1.0 - JA
	// v2.0 - JA (taboo)
	public Review save(final Review review) {
		Assert.notNull(review);

		//Check for taboo
		final Boolean containsTaboo = this.sysConfigService.containsTaboo(review.getBody());
		review.setContainsTaboo(containsTaboo);

		//Beware to change this method! SystemConfigurationService.updateTaboo uses it!
		return this.reviewRepository.save(review);
	}

	/* v1.0 - josembell */
	public Review findOne(final int reviewId) {
		return this.reviewRepository.findOne(reviewId);
	}

	/* v1.0 - josembell */
	public void delete(final Review review) {
		Assert.notNull(review);
		final Administrator administrator = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		this.reviewRepository.delete(review);

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

	/* v1.0 - josembell */
	public Review reconstruct(final Review prunedReview, final BindingResult binding) {
		Assert.notNull(prunedReview);
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		Review review = new Review();

		if (prunedReview.getId() == 0) {
			review = prunedReview;
			review.setCreationDate(new Date(System.currentTimeMillis() - 1000));
			review.setVisitor(visitor);
			review.setContainsTaboo(false);
		}

		this.validator.validate(review, binding);

		return review;
	}
}
