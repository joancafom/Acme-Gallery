
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ExhibitionRepository;
import domain.Exhibition;

@Component
@Transactional
public class StringToExhibitionConverter implements Converter<String, Exhibition> {

	@Autowired
	ExhibitionRepository	exhibitionRepository;


	@Override
	public Exhibition convert(final String text) {
		final Exhibition result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.exhibitionRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
