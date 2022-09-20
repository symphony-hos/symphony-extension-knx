package io.symphony.knx;

import io.symphony.extension.event.EnableSymphonyEventHandling;
import io.symphony.extension.point.EnableSymphonyPoint;
import io.symphony.extension.startup.EnableSymphonyStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableSymphonyPoint // Repo and Controller
@EnableSymphonyEventHandling
@EnableSymphonyStartup
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
		    
}
