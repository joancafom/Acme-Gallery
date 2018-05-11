
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

}
