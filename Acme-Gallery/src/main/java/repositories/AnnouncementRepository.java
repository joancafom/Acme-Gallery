
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	// v1.0 - JA
	@Query("select a from Announcement a where a.containsTaboo = true")
	Collection<Announcement> findTabooed();

	// v1.0 - JA
	@Query("select a from Announcement a where a.containsTaboo = true")
	Page<Announcement> findTabooed(Pageable pageable);

}
