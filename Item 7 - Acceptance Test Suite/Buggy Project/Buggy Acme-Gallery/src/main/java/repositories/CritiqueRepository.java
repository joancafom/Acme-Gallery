
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Critique;

@Repository
public interface CritiqueRepository extends JpaRepository<Critique, Integer> {

	// v1.0 - JA
	@Query("select c from Critique c where c.exhibition.id = ?1")
	Collection<Critique> findAllByExhibitionId(int exhibitionId);

	// v1.0 - JA
	@Query("select c from Critique c where c.exhibition.id = ?1")
	Page<Critique> findAllByExhibitionId(int exhibitionId, Pageable pageable);

	// v1.0 - JA
	@Query("select c from Critique c where c.critic.id = ?1 and c.exhibition.id = ?2")
	Critique findByCriticIdAndExhibitionId(final int criticId, final int exhibitionId);

}
