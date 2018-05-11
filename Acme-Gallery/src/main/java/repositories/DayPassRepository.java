
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.DayPass;

@Repository
public interface DayPassRepository extends JpaRepository<DayPass, Integer> {

}
