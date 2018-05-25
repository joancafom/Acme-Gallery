
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.DayPassRepository;
import domain.DayPass;

@Component
@Transactional
public class StringToDayPassConverter implements Converter<String, DayPass> {

	@Autowired
	DayPassRepository	dayPassRepository;


	@Override
	public DayPass convert(final String text) {
		final DayPass result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.dayPassRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
