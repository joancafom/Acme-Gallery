
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Director;

@Component
@Transactional
public class DirectorToStringConverter implements Converter<Director, String> {

	@Override
	public String convert(final Director director) {
		String result;

		if (director == null)
			result = null;
		else
			result = String.valueOf(director.getId());
		return result;
	}

}
