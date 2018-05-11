
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Room;

@Component
@Transactional
public class RoomToStringConverter implements Converter<Room, String> {

	@Override
	public String convert(final Room room) {
		String result;

		if (room == null)
			result = null;
		else
			result = String.valueOf(room.getId());
		return result;
	}

}
