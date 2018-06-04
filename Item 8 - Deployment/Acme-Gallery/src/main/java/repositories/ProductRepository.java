
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	/* v1.0 - josembell */
	@Query("select p from Product p where p.store.id=?1")
	Page<Product> findAllByStore(int storeId, Pageable pageRequest);

}
