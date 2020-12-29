/**
 * 
 */
/**
 * @author Maple
 *
 */
package org.maple.profitsystem;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
@EnableScheduling
public class Application{

	public static ConfigurableApplicationContext springContext;
	
	private static Logger logger = Logger.getLogger(Application.class);
	
	public static void main(String[] args) throws IOException{
		springContext = SpringApplication.run(Application.class, args);
		
		// get ps context
		CollectorContext context = springContext.getBean(CollectorContext.class);

		logger.info("Startup PS Collector");
		
		// Debug statements
		// ---------------
		
		context.run(args);
		
		logger.info("Startup schduled tasks...");
	}

}