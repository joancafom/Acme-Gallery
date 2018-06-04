
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

	// v1.0 - Alicia
	@Query("select s from Store s where s.museum.id = ?1")
	Store getStoreByMuseumId(int museumId);
}
