package io.symphony.knx.properties;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class IPConnectionProperties {

	public static enum IPConnectionType {
		ROUTING, TUNNEL
	}
	
	private IPConnectionProperties.IPConnectionType type;

	private String remoteAddress;

	private Integer remotePort = 3671;

	private String localAddress = null;

	private Integer localPort = null;
	
	private String localSource = "1.15.25";

	private Boolean nat = false;
	
}