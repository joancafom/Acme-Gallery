
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Visitor;

@Component
@Transactional
public class VisitorToStringConverter implements Converter<Visitor, String> {

	@Override
	public String convert(final Visitor visitor) {
		String result;

		if (visitor == null)
			result = null;
		else
			result = String.valueOf(visitor.getId());
		return result;
	}

}
