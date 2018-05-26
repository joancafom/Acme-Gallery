
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Critic;

@Repository
public interface CriticRepository extends JpaRepository<Critic, Integer> {

}
