
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Store;

@Component
@Transactional
public class StoreToStringConverter implements Converter<Store, String> {

	@Override
	public String convert(final Store store) {
		String result;

		if (store == null)
			result = null;
		else
			result = String.valueOf(store.getId());
		return result;
	}

}
