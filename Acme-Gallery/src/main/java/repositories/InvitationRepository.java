
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

	@Query("select i from Invitation i where i.guest.id = ?1 order by i.sentMoment DESC")
	Collection<Invitation> findAllReceivedByVisitorId(final int visitorId);

	@Query("select i from Invitation i where i.guest.id = ?1 order by i.sentMoment DESC")
	Page<Invitation> findAllReceivedByVisitorId(final int visitorId, Pageable pageable);
}
