package io.symphony.knx.properties;

import io.symphony.extension.properties.ExtensionProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "symphony.knx")
public class KNXExtensionProperties extends ExtensionProperties<KNXPointConfigProperties> {

	private ConnectionProperties connection;

}
