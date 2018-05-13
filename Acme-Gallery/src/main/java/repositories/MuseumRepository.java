
package repositories;

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

}
