
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
	@Query("select h from Artwork h where h.exhibition.id = ?1")
	Collection<Artwork> findAllByExhibitionId(int exhibitionId);

	// v1.0 - JA
	@Query("select h from Artwork h where h.exhibition.id = ?1")
	Page<Artwork> findAllByExhibitionId(int exhibitionId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select h from Artwork h where h.exhibition.id = ?1 and h.isFinal = true")
	Collection<Artwork> findFinalByExhibitionId(int exhibitionId);

	// v1.0 - Alicia
	@Query("select h from Artwork h where h.exhibition.id = ?1 and h.isFinal = true")
	Page<Artwork> findFinalByExhibitionId(int exhibitionId, Pageable pageable);
}
