package io.symphony.knx.properties;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class ConnectionProperties {
	
	private IPConnectionProperties ip;

}