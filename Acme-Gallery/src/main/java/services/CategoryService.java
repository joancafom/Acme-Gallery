
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Category;

@Service
@Transactional
public class CategoryService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private CategoryRepository	categoryRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Category findOne(final int categoryId) {
		return this.categoryRepository.findOne(categoryId);
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - Alicia
	public Collection<Category> getChildren(final Category category) {
		Assert.notNull(category);

		final Collection<Category> res = this.categoryRepository.findChildren(category.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Category> getChildren(final Category category, final int page, final int size) {
		Assert.notNull(category);

		final Page<Category> res = this.categoryRepository.findChildren(category.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Category getRootCategory() {
		final Category res = this.categoryRepository.findRootCategory();
		Assert.notNull(res);

		return res;
	}
}
