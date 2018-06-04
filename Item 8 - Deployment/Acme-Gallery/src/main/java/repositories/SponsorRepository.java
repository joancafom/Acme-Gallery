
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	// v1.0 - Alicia
	@Query("select s from Sponsor s where s.userAccount.isLocked=true")
	Collection<Sponsor> findAllLocked();

	/* v1.0 - josembell */
	@Query("select s from Sponsor s where s.userAccount.isLocked=true")
	Page<Sponsor> findAllLocked(Pageable pageRequest);

	// v1.0 - Alicia
	@Query("select s from Sponsor s where s.userAccount.isLocked=false")
	Collection<Sponsor> findAllUnlocked();

	/* v1.0 - josembell */
	@Query("select s from Sponsor s where s.userAccount.isLocked=false")
	Page<Sponsor> findAllUnlocked(Pageable pageRequest);
}
