package io.symphony.knx.client.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import tuwien.auto.calimero.dptxlator.DPT;

@Component
@WritingConverter
public class DptToStringConverter implements Converter<DPT, String> {

	@Override
	public String convert(DPT source) {
		if (source == null)
			return null;
		return source.getID();
	}
	
}