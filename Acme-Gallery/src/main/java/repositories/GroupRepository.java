
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

	@Query("select g from Group g where g.containsTaboo = true")
	Collection<Group> findTabooed();

	@Query("select g from Group g where g.containsTaboo = true")
	Page<Group> findTabooed(Pageable pageable);

	@Query("select g from Group g where g.containsTaboo = false")
	Collection<Group> findNotTabooed();

	@Query("select g from Group g where g.containsTaboo = false")
	Page<Group> findNotTabooed(Pageable pageable);
}
