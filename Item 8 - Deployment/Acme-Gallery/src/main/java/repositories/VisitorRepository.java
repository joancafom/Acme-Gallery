
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Group;
import domain.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

	/* v1.0 - josembell */
	@Query("select v from Visitor v, Group g where g.id=?1 and v member of g.participants")
	Page<Visitor> findAllByGroup(int id, Pageable pageRequest);

	// v1.0 - Alicia
	@Query("select v from Visitor v where v.userAccount.isLocked=true")
	Collection<Visitor> findAllLocked();

	/* v1.0 - josembell */
	@Query("select v from Visitor v where v.userAccount.isLocked=true")
	Page<Visitor> findAllLocked(Pageable pageRequest);

	// v1.0 - Alicia
	@Query("select v from Visitor v where v.userAccount.isLocked=false")
	Collection<Visitor> findAllUnlocked();

	/* v1.0 - josembell */
	@Query("select v from Visitor v where v.userAccount.isLocked=false")
	Page<Visitor> findAllUnlocked(Pageable pageRequest);

	//v1.0 - JA
	@Query("select v from Visitor v where ?1 not member of v.joinedGroups and v not in (select i.guest from Invitation i where i.group = ?1 and (i.isAccepted = null))")
	Collection<Visitor> findRemainingByGroup(final Group group);

	//v1.0 - JA
	@Query("select v from Visitor v where v.id = ?1 and ?2 member of v.joinedGroups or v in (select i.guest from Invitation i where i.group = ?2 and (i.isAccepted = null) and i.guest.id = ?1)")
	Visitor fulfillReqsNotToBeSent(final int visitorId, final Group group);

}
