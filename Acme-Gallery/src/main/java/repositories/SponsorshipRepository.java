
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s where s.exhibition.id = ?1 order by s.status ASC")
	Collection<Sponsorship> findAllByExhibitionId(final int exhibitionId);

	@Query("select s from Sponsorship s where s.exhibition.id = ?1 order by s.status ASC")
	Page<Sponsorship> findAllByExhibitionId(final int exhibitionId, Pageable pageable);
}
