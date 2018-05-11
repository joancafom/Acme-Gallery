
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Reviewer;

@Component
@Transactional
public class ReviewerToStringConverter implements Converter<Reviewer, String> {

	@Override
	public String convert(final Reviewer reviewer) {
		String result;

		if (reviewer == null)
			result = null;
		else
			result = String.valueOf(reviewer.getId());
		return result;
	}

}
