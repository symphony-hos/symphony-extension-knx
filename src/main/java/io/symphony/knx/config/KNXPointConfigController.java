package io.symphony.knx.config;

import io.symphony.extension.config.rest.AbstractPointConfigController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.stereotype.Component;

@Component
public class KNXPointConfigController extends AbstractPointConfigController<KNXPointConfig> {

	public KNXPointConfigController(@Autowired EntityLinks links, @Autowired KNXPointConfigRepo configRepo, @Autowired KNXPointConfigUpdater updateHandler) {
		super(links, configRepo, updateHandler);
	}

}
