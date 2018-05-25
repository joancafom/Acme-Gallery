
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

	// v2.0 - Alicia
	@Query("select g from Guide g, Museum m where m.id = ?1 and g member of m.guides and ?2 not member of g.exhibitions")
	Collection<Guide> findByMuseumIdNotInExhibition(int museumId, Exhibition exhibition);

	// v1.0 - JA
	@Query("select g from Guide g, Museum m where m.id = ?1 and g not member of m.guides")
	Collection<Guide> findNotWorkingByMuseumId(int museumId);

}
