
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Museum;

@Repository
public interface MuseumRepository extends JpaRepository<Museum, Integer> {

	// v1.0 - Alicia
	@Query("select m from Museum m")
	Page<Museum> findAllPaginated(int categoryId, Pageable pageable);

	/* v1.0 - josembell */
	@Query("select m from Museum m where m.director.id=?1")
	Page<Museum> findAllByDirector(int idPageable, Pageable pageRequest);

	// v1.0 - Alicia
	@Query("select m from Museum m where m.director.id = ?1")
	Collection<Museum> findByDirectorId(int directorId);

}
