
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

}
