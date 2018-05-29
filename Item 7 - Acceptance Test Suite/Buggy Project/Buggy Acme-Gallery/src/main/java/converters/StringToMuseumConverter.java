
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.MuseumRepository;
import domain.Museum;

@Component
@Transactional
public class StringToMuseumConverter implements Converter<String, Museum> {

	@Autowired
	MuseumRepository	museumRepository;


	@Override
	public Museum convert(final String text) {
		final Museum result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.museumRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
