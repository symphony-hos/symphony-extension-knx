package io.symphony.knx.startup;

import io.symphony.extension.startup.AbstractPointInitializer;
import io.symphony.knx.client.KNXClient;
import io.symphony.knx.config.KNXPointConfig;
import io.symphony.knx.config.KNXPointConfigRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class KNXPointInitializer extends AbstractPointInitializer<KNXPointConfig> {

	private Logger logger = LoggerFactory.getLogger(KNXPointInitializer.class);
		
	private final KNXClient knxClient;

	public KNXPointInitializer(@Autowired KNXPointConfigRepo configRepo, @Autowired KNXClient knxClient) {
		super(configRepo);
		this.knxClient = knxClient;
	}

	@Override
	public void initialize(KNXPointConfig config) {
		if (config.getRead() == null || config.getDpt() == null) {
			logger.info("Point config {} is not valid for initialization. Either read address for DPT is missing", config.getId());
			return;
		}
		knxClient.read(config.getDpt(), config.getRead());
	}

}
