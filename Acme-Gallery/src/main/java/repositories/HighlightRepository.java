
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Highlight;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Integer> {

	// v1.0 - JA
	@Query("select h from Highlight h where h.exhibition.id = ?1")
	Collection<Highlight> findAllByExhibitionId(int exhibitionId);

	// v1.0 - JA
	@Query("select h from Highlight h where h.exhibition.id = ?1")
	Page<Highlight> findAllByExhibitionId(int exhibitionId, Pageable pageable);
}
