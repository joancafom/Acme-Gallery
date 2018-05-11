
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Critique;

@Component
@Transactional
public class CritiqueToStringConverter implements Converter<Critique, String> {

	@Override
	public String convert(final Critique critique) {
		String result;

		if (critique == null)
			result = null;
		else
			result = String.valueOf(critique.getId());
		return result;
	}

}
