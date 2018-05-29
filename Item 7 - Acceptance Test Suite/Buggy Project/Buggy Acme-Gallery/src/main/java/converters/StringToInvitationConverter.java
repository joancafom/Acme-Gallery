
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.InvitationRepository;
import domain.Invitation;

@Component
@Transactional
public class StringToInvitationConverter implements Converter<String, Invitation> {

	@Autowired
	InvitationRepository	invitationRepository;


	@Override
	public Invitation convert(final String text) {
		final Invitation result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.invitationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
