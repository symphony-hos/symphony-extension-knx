package io.symphony.knx.properties;

import javax.validation.constraints.NotNull;

import io.symphony.extension.config.data.PointConfig;
import io.symphony.knx.config.KNXPointConfig;
import org.springframework.validation.annotation.Validated;

import io.symphony.extension.properties.PointProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.dptxlator.DPT;

@Data
@EqualsAndHashCode(callSuper=false)
@Validated
public class KNXPointConfigProperties extends PointProperties {
	
	private GroupAddress read;
	
	private GroupAddress write;
	
	@NotNull
	private DPT dpt;

	@Override
	public PointConfig toPointConfig() {
		return KNXPointConfig.builder()
			.id(getId())
			.read(read)
			.write(write)
			.dpt(dpt)
			.build();
	}

}