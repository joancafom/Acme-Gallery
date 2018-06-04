
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Group;

@Component
@Transactional
public class GroupToStringConverter implements Converter<Group, String> {

	@Override
	public String convert(final Group group) {
		String result;

		if (group == null)
			result = null;
		else
			result = String.valueOf(group.getId());
		return result;
	}

}
