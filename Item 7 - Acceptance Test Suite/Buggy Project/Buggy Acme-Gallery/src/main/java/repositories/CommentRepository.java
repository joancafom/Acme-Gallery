
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c where c.containsTaboo = true")
	Collection<Comment> findTabooed();

	@Query("select c from Comment c where c.containsTaboo = true")
	Page<Comment> findTabooed(Pageable pageable);

	@Query("select c from Comment c where c.containsTaboo = false")
	Collection<Comment> findNotTabooed();

	@Query("select c from Comment c where c.containsTaboo = false")
	Page<Comment> findNotTabooed(Pageable pageable);

	/* v1.0 - josembell */
	@Query("select c from Comment c where c.group.id=?1 and c.parentComment=null")
	Page<Comment> findAllRootByGroup(int id, Pageable pageRequest);

	/* v1.0 - josembell */
	@Query("select c from Comment c where c.parentComment.id=?1")
	Page<Comment> findRepliesByComment(int id, Pageable pageRequest);

}
