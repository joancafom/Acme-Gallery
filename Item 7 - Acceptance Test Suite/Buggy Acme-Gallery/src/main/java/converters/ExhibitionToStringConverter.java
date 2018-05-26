
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Exhibition;

@Component
@Transactional
public class ExhibitionToStringConverter implements Converter<Exhibition, String> {

	@Override
	public String convert(final Exhibition exhibition) {
		String result;

		if (exhibition == null)
			result = null;
		else
			result = String.valueOf(exhibition.getId());
		return result;
	}

}
