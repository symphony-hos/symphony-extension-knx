package io.symphony.knx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.symphony.knx.client.KNXIPClient;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

@SpringBootTest
public class KNXIPClientTests {

	@Autowired
	private KNXIPClient client;
	
	@Test
	public void write() throws KNXFormatException {
		
		DPT dpt = DPTXlatorBoolean.DPT_SWITCH;
		DPTXlatorBoolean xlator = new DPTXlatorBoolean(dpt);		
		xlator.setValue(true);
		
		GroupAddress ga = new GroupAddress("0/1/14");
		
		
		client.write(ga, xlator);
		
	}
	
}
