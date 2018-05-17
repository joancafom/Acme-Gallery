
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.museum.director.id = ?1 order by i.isChecked asc")
	Collection<Incident> findByDirectorId(int directorId);

}
