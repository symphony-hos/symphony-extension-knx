package io.symphony.knx.sync;

import io.symphony.common.point.IPoint;
import io.symphony.common.point.IQuantityPoint;
import io.symphony.common.point.IStatePoint;
import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.data.state.*;
import io.symphony.common.point.data.state.type.*;
import io.symphony.extension.event.PointPublisher;
import io.symphony.extension.point.data.PointRepo;
import io.symphony.extension.sync.System2PointSynchronizer;
import io.symphony.knx.client.GroupAddressListener;
import io.symphony.knx.client.KNXClient;
import io.symphony.knx.config.KNXPointConfig;
import io.symphony.knx.config.KNXPointConfigRepo;
import io.symphony.knx.client.utils.TypeConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.IndividualAddress;
import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KNX2PointSynchronizer implements System2PointSynchronizer, GroupAddressListener {

	private Logger logger = LoggerFactory.getLogger(KNX2PointSynchronizer.class);

	private final KNXClient knxClient;

	private final PointRepo pointRepo;

	private final KNXPointConfigRepo configRepo;

	private final PointPublisher publisher;


	@PostConstruct
	private void setUp() {
		this.knxClient.addGroupAddressListener(this);
	}

	@PreDestroy
	private void tearDown() {
		this.knxClient.removeGroupAddressListener(this);
	}

	/*
	 * GroupAddressListener
	 */

	@Override
	public void groupWrite(IndividualAddress source, GroupAddress destination, byte[] asdu) {
		handleIncoming(source, destination, asdu);
	}

	@Override
	public void groupReadResponse(IndividualAddress source, GroupAddress destination, byte[] asdu) {
		handleIncoming(source, destination, asdu);
	}

	private void handleIncoming(IndividualAddress source, GroupAddress destination, byte[] asdu) {
		logger.debug("Received groupWrite: {} {} {}", source, destination, asdu);

		// Find all point configs that have the "destination" as the read address
		Iterable<KNXPointConfig> configs = configRepo.findByRead(destination);

		// For every config, find all points that reference it
		for (KNXPointConfig config : configs) {
			Point point = config.getPoint();
			DPT dpt = config.getDpt();
			updatePoint(dpt, point, asdu);
			toPoint(point);
		}
	}

	private IPoint updatePoint(DPT dpt, IPoint point, byte[] asdu) {
		assert (point != null);

		DPTXlator xlator = TypeConverter.toXlator(dpt, asdu);

		if (point instanceof QuantityPoint)
			updateQuantity((QuantityPoint) point, dpt, xlator);
		else if (point instanceof IStatePoint)
			updateState((IStatePoint<?>) point, xlator);

		return point;
	}

	private IQuantityPoint updateQuantity(IQuantityPoint point, DPT dpt, DPTXlator xlator) {
		point.setUnit(TypeConverter.dptToUnit(dpt));
		try {
			point.setValue(xlator.getNumericValue());
		} catch (KNXFormatException e) {
			logger.warn("An error occurred updating the state of point {} with value {}", point.getId(),
					xlator.getValue(), e);
			return null;
		}
		logger.info("Updated point {} to value {}", point.getId(), point.getValue());
		return point;
	}

	private IStatePoint<? extends State> updateState(IStatePoint<? extends State> point, DPTXlator xlator) {
		
		if (xlator instanceof DPTXlatorBoolean) {
			DPTXlatorBoolean boolXlator = (DPTXlatorBoolean) xlator;
			Boolean value = boolXlator.getValueBoolean();
			
			if (point instanceof SwitchPoint) {
				SwitchPoint p = (SwitchPoint) point;
				p.setState(Switch.fromBoolean(value));
				return p;
				
			} else if (point instanceof MotionPoint) {
				MotionPoint p = (MotionPoint) point;
				p.setState(Motion.fromBoolean(value));
				return p;
				
			} else if (point instanceof VerticalDirectionPoint) {
				VerticalDirectionPoint p = (VerticalDirectionPoint) point;
				p.setState(VerticalDirection.fromBoolean(value));
				return p;
				
			} else if (point instanceof AlarmPoint) {
				AlarmPoint p = (AlarmPoint) point;
				p.setState(Alarm.fromBoolean(value));
				return p;
				
			} else if (point instanceof ContactPoint) {
				ContactPoint p = (ContactPoint) point;
				p.setState(Contact.fromBoolean(value));
				return p;
			}
		}
		
		logger.warn("Unable to update state on point {}", point.getId());
		return null;
	}

	@Override
	public boolean subscribesTo(GroupAddress address) {
		long num = StreamSupport.stream(configRepo.findByRead(address).spliterator(), false).count();
		return num > 0;
	}

	@Override
	public void toPoint(Point point) {
		if (point == null)  return;
		publisher.publish(pointRepo.save(point));
	}

}