
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Exhibition;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.identifier like %?1% or e.title like %?1% or e.description like %?1% order by e.endingDate desc")
	Collection<Exhibition> findByKeyword(String keyword);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.identifier like %?1% or e.title like %?1% or e.description like %?1% order by e.endingDate desc")
	Page<Exhibition> findByKeyword(String keyword, Pageable pageable);

}
