
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DayPass;

@Repository
public interface DayPassRepository extends JpaRepository<DayPass, Integer> {

	// v1.0 - Alicia
	@Query("select dp from DayPass dp where dp.visitor.id = ?1")
	Collection<DayPass> findByVisitorId(int visitorId);

	// v1.0 - Alicia
	@Query("select dp from DayPass dp where dp.visitor.id = ?1")
	Page<DayPass> findByVisitorId(int visitorId, Pageable pageable);

}
