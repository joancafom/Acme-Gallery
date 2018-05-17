
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ArtworkRepository;
import domain.Artwork;

@Component
@Transactional
public class StringToArtworkConverter implements Converter<String, Artwork> {

	@Autowired
	ArtworkRepository	artworkRepository;


	@Override
	public Artwork convert(final String text) {
		final Artwork result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.artworkRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
