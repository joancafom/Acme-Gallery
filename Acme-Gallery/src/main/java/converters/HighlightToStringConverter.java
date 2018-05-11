
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Highlight;

@Component
@Transactional
public class HighlightToStringConverter implements Converter<Highlight, String> {

	@Override
	public String convert(final Highlight highlight) {
		String result;

		if (highlight == null)
			result = null;
		else
			result = String.valueOf(highlight.getId());
		return result;
	}

}
