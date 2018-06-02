
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

	// v1.0 - JA
	@Query("select a from Announcement a where a.containsTaboo = false")
	Collection<Announcement> findNotTabooed();

	// v1.0 - JA
	@Query("select a from Announcement a where a.containsTaboo = false")
	Page<Announcement> findNotTabooed(Pageable pageable);

	/* v1.0 - josembell */
	@Query("select a from Announcement a where a.group.id=?1 order by a.creationMoment desc")
	Page<Announcement> findAllByGroup(int id, Pageable pageRequest);

	@Query("select a from Announcement a, Visitor v where v.id=?1 and v member of a.group.participants order by a.creationMoment desc")
	Page<Announcement> getStreamByPrincipal(int id, Pageable pageRequest);

	//v1.0 - JA
	@Query("select a from Announcement a, Visitor v where v.id=?1 and v member of a.group.participants order by a.creationMoment desc")
	Collection<Announcement> getStreamByPrincipal(int principalId);

}
