
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// v1.0 - Implemented by JA
	@Query("select r from Review r where r.museum.id = ?1")
	Page<Review> findAllByMuseumId(final int museumId, Pageable pageable);

}
