
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.IncidentRepository;
import domain.Incident;

@Component
@Transactional
public class StringToIncidentConverter implements Converter<String, Incident> {

	@Autowired
	IncidentRepository	incidentRepository;


	@Override
	public Incident convert(final String text) {
		final Incident result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.incidentRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
