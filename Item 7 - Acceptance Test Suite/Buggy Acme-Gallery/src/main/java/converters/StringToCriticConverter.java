
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.CriticRepository;
import domain.Critic;

@Component
@Transactional
public class StringToCriticConverter implements Converter<String, Critic> {

	@Autowired
	CriticRepository	criticRepository;


	@Override
	public Critic convert(final String text) {
		final Critic result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.criticRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
