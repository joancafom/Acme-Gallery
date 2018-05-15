
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	// v1.0 - Alicia
	@Query("select r from Room r where r.museum.director.id = ?1 and r.isAvailable = true")
	Collection<Room> findAvailableByDirectorId(int directorId);

}
