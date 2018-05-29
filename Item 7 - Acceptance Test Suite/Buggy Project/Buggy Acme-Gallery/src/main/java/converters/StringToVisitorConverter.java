
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.VisitorRepository;
import domain.Visitor;

@Component
@Transactional
public class StringToVisitorConverter implements Converter<String, Visitor> {

	@Autowired
	VisitorRepository	visitorRepository;


	@Override
	public Visitor convert(final String text) {
		final Visitor result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.visitorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
