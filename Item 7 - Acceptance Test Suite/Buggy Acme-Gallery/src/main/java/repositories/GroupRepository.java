
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
public interface GroupRepository extends JpaRepository<Group, Integer> {

	// v1.0 - Implemented by JA
	@Query("select g from Group g where g.isClosed = false or (g.isClosed = true and ?1 member of g.participants)")
	Page<Group> findAllOpenAndMember(Visitor visitor, Pageable pageable);

	@Query("select g from Group g where g.containsTaboo = true")
	Collection<Group> findTabooed();

	@Query("select g from Group g where g.containsTaboo = true")
	Page<Group> findTabooed(Pageable pageable);

	@Query("select g from Group g where g.containsTaboo = false")
	Collection<Group> findNotTabooed();

	@Query("select g from Group g where g.containsTaboo = false")
	Page<Group> findNotTabooed(Pageable pageable);

	/* v1.0 - josembell */
	@Query("select g from Group g where g.museum.id=?1")
	Page<Group> findByMuseum(int id, Pageable pageRequest);

	/* v1.0 - josembell */
	@Query("select g from Group g where g.isClosed=false")
	Page<Group> findAllOpen(Pageable pageRequest);

	/* v1.0 - josembell */
	@Query("select g from Group g, Visitor v where v.id=?1 and v member of g.participants")
	Page<Group> findAllJoinedByPrinciapl(int id, Pageable pageRequest);

	@Query("select g from Group g where g.creator.id=?1")
	Page<Group> findAllCreatedByPrinciapl(int id, Pageable pageRequest);
}
