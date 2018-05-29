
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Incident;

@Component
@Transactional
public class IncidentToStringConverter implements Converter<Incident, String> {

	@Override
	public String convert(final Incident incident) {
		String result;

		if (incident == null)
			result = null;
		else
			result = String.valueOf(incident.getId());
		return result;
	}

}
