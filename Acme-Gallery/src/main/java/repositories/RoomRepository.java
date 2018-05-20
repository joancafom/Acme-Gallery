
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	// v1.0 - Alicia
	@Query("select r from Room r where r.museum.director.id = ?1")
	Collection<Room> findByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select r from Room r where r.museum.director.id = ?1")
	Page<Room> findByDirectorId(int directorId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select r from Room r where r.museum.id = ?1")
	Collection<Room> findByMuseumId(int museumId);

	// v1.0 - Alicia
	@Query("select r from Room r where r.museum.id = ?1")
	Page<Room> findByMuseumId(int museumId, Pageable pageable);

}
