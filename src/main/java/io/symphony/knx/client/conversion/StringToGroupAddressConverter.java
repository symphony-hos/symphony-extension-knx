package io.symphony.knx.client.conversion;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXFormatException;

@Component
@ReadingConverter
@ConfigurationPropertiesBinding
public class StringToGroupAddressConverter implements Converter<String, GroupAddress> {

	@Override
	public GroupAddress convert(String source) {
		if (source == null || source.trim().length() == 0)
			return null;
		try {
			return new GroupAddress(source);
		} catch (KNXFormatException e) {
			throw new RuntimeException(e);
		}
	}
	
}