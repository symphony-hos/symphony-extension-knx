package io.symphony.knx.sync;

import io.symphony.common.point.data.Point;
import io.symphony.extension.sync.Point2SystemSynchronizer;
import io.symphony.knx.client.KNXClient;
import io.symphony.knx.config.KNXPointConfig;
import io.symphony.knx.config.KNXPointConfigRepo;
import io.symphony.knx.client.utils.TypeConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tuwien.auto.calimero.dptxlator.DPTXlator;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Point2KNXSynchronizer implements Point2SystemSynchronizer {

	private Logger logger = LoggerFactory.getLogger(Point2KNXSynchronizer.class);

	private final KNXPointConfigRepo configRepo;

	private final KNXClient client;

	@Override
	public void toSystem(Point point) {
		Optional<KNXPointConfig> configOpt = configRepo.findByPoint(point);

		if (configOpt.isEmpty()) {
			logger.warn("Unable to sync point {} to KNX. No config found.", point.getId());
			return;
		}

		KNXPointConfig config = configOpt.get();

		if (config.getWrite() == null) {
			logger.warn("Config of point {} does not specify a write address", point.getId());
			return;
		}

		if (config.getDpt() == null) {
			logger.warn("Config of point {} does not specify a DPT", point.getId());
			return;
		}

		// 
		logger.info("Publishing point to KNX: {}", point.getId());

		// Convert value to "loaded" DPTXlator and put the message on the bus
		DPTXlator xlator;
		try {
			xlator = TypeConverter.toXlator(point, config.getDpt());
		} catch (Exception e) {
			throw new RuntimeException("An error occurred trying to translate point " + point.getId() + " to a dpt translator.", e);
		}
		client.write(config.getWrite(), xlator);
	}

}
