
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

	/* v1.0 - josembell */
	@Query("select v from Visitor v, Group g where g.id=?1 and v member of g.participants")
	Page<Visitor> findAllByGroup(int id, Pageable pageRequest);

	/* v1.0 - josembell */
	@Query("select v from Visitor v where v.userAccount.isLocked=true")
	Page<Visitor> findAllLocked(Pageable pageRequest);

	/* v1.0 - josembell */
	@Query("select v from Visitor v where v.userAccount.isLocked=false")
	Page<Visitor> findAllUnlocked(Pageable pageRequest);

}
