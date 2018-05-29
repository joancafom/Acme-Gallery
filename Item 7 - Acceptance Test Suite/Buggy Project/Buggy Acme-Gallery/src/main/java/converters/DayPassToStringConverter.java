
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.DayPass;

@Component
@Transactional
public class DayPassToStringConverter implements Converter<DayPass, String> {

	@Override
	public String convert(final DayPass dayPass) {
		String result;

		if (dayPass == null)
			result = null;
		else
			result = String.valueOf(dayPass.getId());
		return result;
	}

}
