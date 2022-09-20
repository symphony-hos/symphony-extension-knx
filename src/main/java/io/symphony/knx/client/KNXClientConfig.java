package io.symphony.knx.client;

import io.symphony.knx.properties.KNXExtensionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KNXClientConfig {

    @Bean
    public KNXClient knxClient(KNXExtensionProperties properties) {
        KNXClient client = null;
        if (properties.getConnection().getIp() != null)
            client = new KNXIPClient(properties);
        return client;
    }

}
