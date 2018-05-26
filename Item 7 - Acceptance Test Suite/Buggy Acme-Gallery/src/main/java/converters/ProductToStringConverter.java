
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Product;

@Component
@Transactional
public class ProductToStringConverter implements Converter<Product, String> {

	@Override
	public String convert(final Product product) {
		String result;

		if (product == null)
			result = null;
		else
			result = String.valueOf(product.getId());
		return result;
	}

}
