
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

}
