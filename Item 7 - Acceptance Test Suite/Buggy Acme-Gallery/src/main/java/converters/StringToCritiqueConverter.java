
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.CritiqueRepository;
import domain.Critique;

@Component
@Transactional
public class StringToCritiqueConverter implements Converter<String, Critique> {

	@Autowired
	CritiqueRepository	critiqueRepository;


	@Override
	public Critique convert(final String text) {
		final Critique result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.critiqueRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
