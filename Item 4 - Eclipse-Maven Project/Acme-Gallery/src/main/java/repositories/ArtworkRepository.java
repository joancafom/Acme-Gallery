
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Integer> {

	// v1.0 - JA
	@Query("select a from Artwork a where a.exhibition.id = ?1 order by a.isHighlight DESC")
	Collection<Artwork> findAllByExhibitionId(int exhibitionId);

	// v1.0 - JA
	@Query("select a from Artwork a where a.exhibition.id = ?1 order by a.isHighlight DESC")
	Page<Artwork> findAllByExhibitionId(int exhibitionId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select a from Artwork a where a.exhibition.id = ?1 and a.isFinal = true order by a.isHighlight DESC")
	Collection<Artwork> findFinalByExhibitionId(int exhibitionId);

	// v1.0 - Alicia
	@Query("select a from Artwork a where a.exhibition.id = ?1 and a.isFinal = true order by a.isHighlight DESC")
	Page<Artwork> findFinalByExhibitionId(int exhibitionId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select a from Artwork a where a.exhibition.id = ?1")
	Collection<Artwork> findByExhibitionId(int exhibitionId);
}
