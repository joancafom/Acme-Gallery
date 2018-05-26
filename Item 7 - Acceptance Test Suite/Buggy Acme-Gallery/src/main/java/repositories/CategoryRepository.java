
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	//v1.0 - Implemented by JA
	@Query("select c from Category c where c.parentCategory = ?1 and c.name = ?2")
	Category findByParentAndName(final Category parentCategory, final String name);

	// v1.0 - Alicia
	@Query("select c from Category c where c.parentCategory.id = ?1")
	Collection<Category> findChildren(int categoryId);

	// v1.0 - Alicia
	@Query("select c from Category c where c.parentCategory.id = ?1")
	Page<Category> findChildren(int categoryId, Pageable pageable);

	// v1.0 - Alicia
	@Query("select c from Category c where c.name = 'CATEGORY'")
	Category findRootCategory();

	// v1.0 - Alicia
	@Query("select c from Category c where c.name != 'CATEGORY'")
	Collection<Category> findAllExceptRoot();

}
