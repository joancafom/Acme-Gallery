
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//v1.0 - Implemented by JA
	@Query("select s from Sponsorship s where s.exhibition.id = ?1 order by s.status ASC")
	Collection<Sponsorship> findAllByExhibitionId(final int exhibitionId);

	//v1.0 - Implemented by JA
	@Query("select s from Sponsorship s where s.exhibition.id = ?1 order by s.status ASC")
	Page<Sponsorship> findAllByExhibitionId(final int exhibitionId, Pageable pageable);

	//v1.0 - Implemented by JA
	@Query("select s from Sponsorship s where s.exhibition.id = ?1 and s.startingDate != null and (s.status = 'ACCEPTED' or (s.status = 'TIME_NEGOTIATION' and s.startingDate >= CURRENT_TIMESTAMP)) and ( (?2 >= s.startingDate and ?2 <= s.endingDate) or (?3 >= s.startingDate and ?3 <= s.endingDate) or (s.startingDate >= ?2 and s.endingDate <= ?3))")
	Collection<Sponsorship> findByExhibitionIdAndDates(final int exhibitionId, Date startingDate, Date endingDate);

	//v1.0 - Implemented by JA
	@Query("select s from Sponsorship s where s.status = 'ACCEPTED'and s.exhibition.id = ?1 and s.startingDate <= CURRENT_TIMESTAMP and s.endingDate >= CURRENT_TIMESTAMP")
	Sponsorship findCurrentByExhibitionId(final int exhibitionId);

	//v1.0 - Implemented by JA
	@Query("select s from Sponsorship s where s.exhibition.id = ?1 and (s.status = 'ACCEPTED' or (s.status = 'TIME_NEGOTIATION' and s.startingDate >= CURRENT_TIMESTAMP))")
	Collection<Sponsorship> findAcceptedAndNegotiationByExhibitionId(final int exhibitionId);

	// v1.0 - Alicia
	@Query("select s from Sponsorship s where s.sponsor.id = ?1 order by s.status asc")
	Collection<Sponsorship> findBySponsorId(int sponsorId);

	// v1.0 - Alicia
	@Query("select s from Sponsorship s where s.sponsor.id = ?1 order by s.status asc")
	Page<Sponsorship> findBySponsorId(int sponsorId, Pageable pageable);
}
