
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ExhibitionRepository;
import domain.Category;
import domain.Exhibition;

@Service
@Transactional
public class ExhibitionService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private ExhibitionRepository	exhibitionRepository;


	// Supporting Services ----------------------------------------------------------------------------

	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - Alicia
	public Collection<Exhibition> getByKeyword(final String keyword) {
		Assert.notNull(keyword);

		final Collection<Exhibition> res = this.exhibitionRepository.findByKeyword(keyword);
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getByKeyword(final String keyword, final int page, final int size) {
		Assert.notNull(keyword);

		final Page<Exhibition> res = this.exhibitionRepository.findByKeyword(keyword, new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Collection<Exhibition> getByCategory(final Category category) {
		Assert.notNull(category);

		final Collection<Exhibition> res = this.exhibitionRepository.findByCategoryId(category.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<Exhibition> getByCategory(final Category category, final int page, final int size) {
		Assert.notNull(category);

		final Page<Exhibition> res = this.exhibitionRepository.findByCategoryId(category.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

}
