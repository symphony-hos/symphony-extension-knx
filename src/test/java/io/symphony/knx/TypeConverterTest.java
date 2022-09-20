package io.symphony.knx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.data.state.SwitchPoint;
import io.symphony.knx.client.utils.TypeConverter;
import tec.units.ri.unit.Units;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

public class TypeConverterTest {

	@Test
	public void testDPT2PointType() {
		DPT dptSwitch = DPTXlatorBoolean.DPT_SWITCH;
		Point switchPoint =TypeConverter.dptToPointInstance(dptSwitch);
		assertTrue(SwitchPoint.class.isAssignableFrom(switchPoint.getClass()));
		
		DPT dptPercent = DPTXlator8BitUnsigned.DPT_SCALING;
		Point percPoint = TypeConverter.dptToPointInstance(dptPercent);
		assertTrue(QuantityPoint.class.isAssignableFrom(percPoint.getClass()));
		assertEquals(Units.PERCENT, ((QuantityPoint) percPoint).getUnit());
	}
	
}
