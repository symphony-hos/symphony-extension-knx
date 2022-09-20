package io.symphony.knx;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import io.symphony.common.conversion.StringToUnitConverter;
import io.symphony.common.conversion.UnitToStringConverter;
import io.symphony.knx.client.conversion.DptToStringConverter;
import io.symphony.knx.client.conversion.GroupAddressToStringConverter;
import io.symphony.knx.client.conversion.StringToDptConverter;
import io.symphony.knx.client.conversion.StringToGroupAddressConverter;

@Configuration
public class CustomMongoConfig {

	  @Bean
	  public MongoCustomConversions mongoCustomConversions() {
	    return new MongoCustomConversions(
	        Arrays.asList(
	            new UnitToStringConverter(),
	            new StringToUnitConverter(),
	            new StringToGroupAddressConverter(),
	            new GroupAddressToStringConverter(),
	            new StringToDptConverter(),
	            new DptToStringConverter()
            )
        );
	  }
	
}
