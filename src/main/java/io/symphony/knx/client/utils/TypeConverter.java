package io.symphony.knx.client.utils;

import java.util.Map;

import javax.measure.Unit;

import io.symphony.common.point.IPoint;
import io.symphony.common.point.data.Point;
import io.symphony.common.point.IQuantityPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.IStatePoint;
import io.symphony.common.point.data.state.MotionPoint;
import io.symphony.common.point.data.state.SwitchPoint;
import io.symphony.common.point.data.state.VerticalDirectionPoint;
import io.symphony.common.point.data.state.type.BooleanState;
import tec.units.ri.unit.Units;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;

public class TypeConverter {
		
	private static final Map<DPT, Class<? extends Point>> dptPointTypeMap = Map.of(
			DPTXlatorBoolean.DPT_SWITCH, SwitchPoint.class,
			DPTXlatorBoolean.DPT_BOOL, MotionPoint.class,
			DPTXlatorBoolean.DPT_UPDOWN, VerticalDirectionPoint.class,
			DPTXlatorBoolean.DPT_STATE, SwitchPoint.class,
			DPTXlator8BitUnsigned.DPT_SCALING, QuantityPoint.class,
			DPTXlator2ByteFloat.DPT_TEMPERATURE, QuantityPoint.class
	);
	
	private static final Map<DPT, Unit<?>> dpt2UnitMap = Map.of(
			DPTXlator8BitUnsigned.DPT_SCALING, Units.PERCENT,
			DPTXlator2ByteFloat.DPT_TEMPERATURE, Units.CELSIUS,
			DPTXlator2ByteFloat.DPT_WIND_SPEED, Units.METRE_PER_SECOND,
			DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT, Units.LUX
	);
	
	
	public static Unit<?> dptToUnit(DPT dpt) {
		return dpt2UnitMap.get(dpt);
	}
	
	
	/* DPT TO POINT */
	
	public static Point dptToPointInstance(DPT dpt) {
		Class<? extends Point> type = dptPointTypeMap.get(dpt);
		if (type == null)
			return null;	
		
		Point p = null;
		
		try {
			p = type.getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create an instance of " + type.getCanonicalName() + ". Failed to invoke the empty constructor.");
		} 

		if (p instanceof QuantityPoint) {
			QuantityPoint q = (QuantityPoint) p;
			Unit<?> unit = dpt2UnitMap.get(dpt);
			if (unit != null)
				q.setUnit(unit);
		}
		
		return p;
	}
	
	
	/* POINT TO XLATOR */
	
	public static DPTXlator toXlator(IPoint point, DPT dpt) throws TypeMapperException, KNXException {
		assert(point != null);
		assert (dpt != null);

		if (point instanceof IQuantityPoint)
			return quantityToXlator((IQuantityPoint) point, dpt);
		else if (point instanceof IStatePoint)
			return stateToXlator((IStatePoint<?>) point, dpt);

		throw new TypeMapperException("Unable to construct DPTXlator from point " + point + " and DPT " + dpt.getID());
	}
	
	private static DPTXlator quantityToXlator(IQuantityPoint point, DPT dpt) throws KNXException {
		DPTXlator xlator = TranslatorTypes.createTranslator(0, dpt.getID());
		xlator.setValue(point.getValue().toString());
		return xlator;
	}
	
	private static DPTXlator stateToXlator(IStatePoint<?> point, DPT dpt) throws KNXException {
		Object state = point.getState();
		if (state instanceof BooleanState) {
			BooleanState<?> boolState = (BooleanState<?>) state;
			DPTXlatorBoolean xlator = (DPTXlatorBoolean) TranslatorTypes.createTranslator(0, dpt.getID());
			if (xlator == null)
				throw new RuntimeException("State is of type boolean type but could not load translator based on DPT \" + dpt.getID()");
			
			// In KNX true means DOWN and false means UP
			boolean value = boolState.toBoolean();
			if (point instanceof VerticalDirectionPoint) {
				value = !value;
			}
			
			xlator.setValue(boolState.toBoolean());
			return xlator;
		}
		throw new RuntimeException("Unsupported state type: " + point.getClass().getCanonicalName());
	}
	
	
	/* BYTES TO XLATOR */

 	public static DPTXlator toXlator(DPT dpt, byte[] asdu) throws TypeMapperException {
		try {
			return TranslatorTypes.createTranslator(dpt, asdu);
		} catch (KNXException e) {
			throw new TypeMapperException("Unable to find translator to handle DPT " + dpt.getID() + " and value " + asdu, e);
		}
	}
	
	
	public static class TypeMapperException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public TypeMapperException(String message) {
			super(message);
		}

		public TypeMapperException(String message, Exception ex) {
			super(message, ex);
		}
	}

}
