
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Museum;

@Component
@Transactional
public class MuseumToStringConverter implements Converter<Museum, String> {

	@Override
	public String convert(final Museum museum) {
		String result;

		if (museum == null)
			result = null;
		else
			result = String.valueOf(museum.getId());
		return result;
	}

}
