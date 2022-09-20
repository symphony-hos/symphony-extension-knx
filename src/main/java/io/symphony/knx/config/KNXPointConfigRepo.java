package io.symphony.knx.config;

import io.symphony.common.point.data.Point;
import io.symphony.extension.config.data.PointConfigRepo;
import tuwien.auto.calimero.GroupAddress;

import java.util.Optional;

public interface KNXPointConfigRepo extends PointConfigRepo<KNXPointConfig> {

	Optional<KNXPointConfig> findByPoint(Point point);

	Iterable<KNXPointConfig> findByRead(GroupAddress address);
	
}
