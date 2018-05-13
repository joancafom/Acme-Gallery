
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Exhibition;
import domain.Guide;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Integer> {

	// v1.0 - JA
	@Query("select g from Guide g where ?1 member of g.exhibitions")
	Collection<Guide> findAllByExhibition(Exhibition exhibition);

	// v1.0 - JA
	@Query("select g from Guide g where ?1 member of g.exhibitions")
	Page<Guide> findAllByExhibition(Exhibition exhibition, Pageable pageable);

}
