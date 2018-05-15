
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import security.LoginService;
import domain.Administrator;
import domain.Category;
import domain.Exhibition;

@Service
@Transactional
public class CategoryService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private CategoryRepository		categoryRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private ExhibitionService		exhibitionService;

	@Autowired
	private AdministratorService	administratorService;

	// Validator --------------------------------------------------------------------------------------
	@Autowired
	private Validator				validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - JA
	public Category create(final Category parentCategory) {

		Assert.notNull(parentCategory);
		Assert.isTrue(this.categoryRepository.exists(parentCategory.getId()));

		final Category res = new Category();

		res.setChildrenCategories(new HashSet<Category>());
		res.setExhibitions(new HashSet<Exhibition>());
		res.setParentCategory(parentCategory);

		return res;
	}

	// v1.0 - JA
	public void delete(final Category category) {

		Assert.notNull(category);
		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		//Root Category cannot be deleted
		Assert.isTrue(!this.getRootCategory().equals(category));

		//Every children category will be deleted as well
		final Collection<Category> childrenCategories = new HashSet<Category>(category.getChildrenCategories());
		for (final Category childCategory : childrenCategories)
			this.delete(childCategory);

		//We re-assign every Exhibition to the parent Category
		final Collection<Exhibition> exhibitions = new HashSet<Exhibition>(category.getExhibitions());
		for (final Exhibition e : exhibitions) {
			e.setCategory(category.getParentCategory());
			this.exhibitionService.save(e);
		}

		this.exhibitionService.flush();

		//We remove this category from the parent
		category.getParentCategory().getChildrenCategories().remove(category);
		this.save(category.getParentCategory());

		this.categoryRepository.delete(category);
	}

	// v1.0 - Alicia
	public Category findOne(final int categoryId) {
		return this.categoryRepository.findOne(categoryId);
	}

	// v1.0 - JA
	public Category save(final Category category) {
		Assert.notNull(category);

		return this.categoryRepository.save(category);
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

	//v1.0 - Implemented by JA
	public Category reconstructEdit(final Category prunedCategory, final BindingResult binding) {

		Assert.notNull(prunedCategory);

		final Category categoryToReconstruct = this.create(prunedCategory.getParentCategory());

		categoryToReconstruct.setName(prunedCategory.getName());

		this.validator.validate(categoryToReconstruct, binding);

		return categoryToReconstruct;
	}

	// v1.0 - JA
	public Category saveCreate(final Category category) {

		Assert.notNull(category);

		//Make sure an Admin is the Actor who is trying to perform the operation
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		//As we are creating, the category must be a brand new one
		Assert.isTrue(category.getId() == 0);

		//The only category allowed to have a null parent is CATEGORY
		Assert.notNull(category.getParentCategory());

		//As it is new, its children categories must be empty
		Assert.notNull(category.getChildrenCategories());
		Assert.isTrue(category.getChildrenCategories().isEmpty());

		//So does happen with exhibitions
		Assert.notNull(category.getExhibitions());
		Assert.isTrue(category.getExhibitions().isEmpty());

		final Category savedCategory = this.save(category);

		//Update the associated parent category
		this.save(category.getParentCategory());

		return savedCategory;
	}

	// v1.0 - Alicia
	public Collection<Category> getAllExceptRoot() {
		final Collection<Category> res = this.categoryRepository.findAllExceptRoot();
		Assert.notNull(res);

		return res;
	}
}
