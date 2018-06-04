
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Invitation;

@Component
@Transactional
public class InvitationToStringConverter implements Converter<Invitation, String> {

	@Override
	public String convert(final Invitation invitation) {
		String result;

		if (invitation == null)
			result = null;
		else
			result = String.valueOf(invitation.getId());
		return result;
	}

}
