
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Guide;

@Component
@Transactional
public class GuideToStringConverter implements Converter<Guide, String> {

	@Override
	public String convert(final Guide guide) {
		String result;

		if (guide == null)
			result = null;
		else
			result = String.valueOf(guide.getId());
		return result;
	}

}
