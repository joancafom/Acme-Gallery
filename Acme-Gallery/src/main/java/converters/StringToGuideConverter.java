
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.GuideRepository;
import domain.Guide;

@Component
@Transactional
public class StringToGuideConverter implements Converter<String, Guide> {

	@Autowired
	GuideRepository	guideRepository;


	@Override
	public Guide convert(final String text) {
		final Guide result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.guideRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
