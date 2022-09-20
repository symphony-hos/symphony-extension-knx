package io.symphony.knx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.symphony.knx.client.conversion.DptToStringConverter;
import io.symphony.knx.client.conversion.StringToDptConverter;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitSigned;

public class DPTConverterTest {
	

	@Test
	public void testToDpt() {
		StringToDptConverter toDpt = new StringToDptConverter();
		String typeId = "9.001";
		DPT dpt = toDpt.convert(typeId);	
		assertEquals(dpt.getID(), typeId);
	}
	
	@Test
	public void testDptToString() {
		DptToStringConverter toStr = new DptToStringConverter();
		DPT dpt = DPTXlator8BitSigned.DPT_PERCENT_V8;
		String str = toStr.convert(dpt);
		assertEquals("6.001", str);
	}
	
}
