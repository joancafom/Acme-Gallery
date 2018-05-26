
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.museum.director.id = ?1 order by i.isChecked asc")
	Collection<Incident> findByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.museum.director.id = ?1 order by i.isChecked asc")
	Page<Incident> findByDirectorId(int directorId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.museum.id = ?1 order by i.isChecked asc")
	Collection<Incident> findByMuseumId(int museumId);

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.museum.id = ?1 order by i.isChecked asc")
	Page<Incident> findByMuseumId(int museumId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.id = ?1 order by i.isChecked asc")
	Collection<Incident> findByRoomId(int roomId);

	// v1.0 - Alicia
	@Query("select i from Incident i where i.room.id = ?1 order by i.isChecked asc")
	Page<Incident> findByRoomId(int roomId, Pageable pageable);

}
