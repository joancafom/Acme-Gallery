
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ReviewRepository;
import domain.Museum;
import domain.Review;

@Service
@Transactional
public class ReviewService extends ActorService {

	//Managed Repository
	@Autowired
	private ReviewRepository	reviewRepository;


	//CRUD Methods

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Page<Review> findAllByMuseum(final Museum museum, final int page, final int size) {

		//Does not have an equivalent as it is bidirectional
		Assert.notNull(museum);
		return this.reviewRepository.findAllByMuseumId(museum.getId(), new PageRequest(page - 1, size));
	}
}
