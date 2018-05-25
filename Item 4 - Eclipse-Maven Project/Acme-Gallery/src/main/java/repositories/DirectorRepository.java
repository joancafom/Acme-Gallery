
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

}
