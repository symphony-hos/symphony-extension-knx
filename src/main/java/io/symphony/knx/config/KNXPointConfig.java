package io.symphony.knx.config;

import io.symphony.common.point.data.Point;
import io.symphony.extension.config.data.PointConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.dptxlator.DPT;

@Data
@NoArgsConstructor
@SuperBuilder
public class KNXPointConfig extends PointConfig {

	private GroupAddress read;

	private GroupAddress write;

	private DPT dpt;

}
