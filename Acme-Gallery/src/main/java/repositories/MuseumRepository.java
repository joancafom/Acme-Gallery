
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Museum;

@Repository
public interface MuseumRepository extends JpaRepository<Museum, Integer> {

}
