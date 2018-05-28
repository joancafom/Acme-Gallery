
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Exhibition;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.ticker like %?1% or e.title like %?1% or e.description like %?1% order by e.endingDate desc")
	Collection<Exhibition> findByKeyword(String keyword);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.ticker like %?1% or e.title like %?1% or e.description like %?1% order by e.endingDate desc")
	Page<Exhibition> findByKeyword(String keyword, Pageable pageable);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.category.id = ?1")
	Collection<Exhibition> findByCategoryId(int categoryId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.category.id = ?1")
	Page<Exhibition> findByCategoryId(int categoryId, Pageable pageable);

	// v1.0 - JA
	@Query("select e from Exhibition e, Room r where r.museum.id = ?1 and e member of r.exhibitions and e.endingDate >= CURRENT_TIMESTAMP")
	Collection<Exhibition> getCurrentAndFutureByMuseumId(int museumId);

	// v1.0 - JA
	@Query("select e from Exhibition e, Room r where r.museum.id = ?1 and e member of r.exhibitions and e.endingDate >= CURRENT_TIMESTAMP")
	Page<Exhibition> getCurrentAndFutureByMuseumId(int museumId, Pageable pageable);

	// v1.0 - JA
	@Query("select r.exhibitions from Room r where r.museum.id = ?1")
	Collection<Exhibition> getAllByMuseumId(int museumId);

	// v1.0 - JA
	@Query("select e from Exhibition e, Room r where r.museum.id = ?1 and e member of r.exhibitions")
	Page<Exhibition> getAllByMuseumId(int museumId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.museum.director.id = ?1")
	Collection<Exhibition> findByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.museum.director.id = ?1")
	Page<Exhibition> findByDirectorId(int directorId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.id = ?1 and e.startingDate <= CURRENT_TIMESTAMP and e.endingDate >= CURRENT_TIMESTAMP")
	Collection<Exhibition> findCurrentByRoomId(int roomId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.id = ?1 order by e.endingDate desc")
	Collection<Exhibition> findByRoomId(int roomId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.id = ?1 order by e.endingDate desc")
	Page<Exhibition> findByRoomId(int roomId, Pageable pageable);

	// v2.0 - Alicia
	@Query("select e from Exhibition e where e.room.id = ?1 and ((e.startingDate <= ?2 and e.endingDate >= ?2) or (e.startingDate <= ?3 and e.endingDate >= ?3))")
	Collection<Exhibition> findDateAndRoomConflicts(int roomId, Date startingDate, Date endingDate);

	// v1.0 - Alicia
	@Query("select e.ticker from Exhibition e where e.room.museum.director.id = ?1")
	Collection<String> findTickersByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select e.startingDate from Exhibition e where e.room.museum.director.id = ?1")
	Collection<Date> findStartingDatesByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select e.endingDate from Exhibition e where e.room.museum.director.id = ?1")
	Collection<Date> findEndingDatesByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select e.room.name from Exhibition e where e.room.museum.director.id = ?1")
	Collection<String> findRoomNamesByDirectorId(int directorId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.id = ?1 and e.startingDate >= CURRENT_TIMESTAMP and e.dayPasses.size > 0")
	Collection<Exhibition> findFutureExhibitionsWithDayPassesByRoomId(int roomId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.id = ?1 and e.startingDate >= CURRENT_TIMESTAMP and e.sponsorships.size > 0")
	Collection<Exhibition> findFutureExhibitionsWithSponsorshipsByRoomId(int roomId);

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.room.museum.director.id = ?1")
	Collection<Exhibition> findExhibitionsByDirectorId(int directorId);

}
