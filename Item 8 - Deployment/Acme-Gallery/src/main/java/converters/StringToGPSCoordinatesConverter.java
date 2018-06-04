
package converters;

import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.GPSCoordinates;

@Component
@Transactional
public class StringToGPSCoordinatesConverter implements Converter<String, GPSCoordinates> {

	@Override
	public GPSCoordinates convert(final String text) {
		GPSCoordinates result;
		String parts[];

		if (text == null)
			result = null;
		else
			try {
				parts = text.split("\\|");
				result = new GPSCoordinates();
				result.setLatitude(Double.valueOf(URLDecoder.decode(parts[0], "UTF-8")));
				result.setLongitude(Double.valueOf(URLDecoder.decode(parts[1], "UTF-8")));
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return result;
	}
}
