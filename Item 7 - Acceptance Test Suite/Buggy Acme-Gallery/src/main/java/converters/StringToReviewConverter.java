
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ReviewRepository;
import domain.Review;

@Component
@Transactional
public class StringToReviewConverter implements Converter<String, Review> {

	@Autowired
	ReviewRepository	reviewRepository;


	@Override
	public Review convert(final String text) {
		final Review result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.reviewRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
