package es.NTTEnterprise.RIntellix.ms_core_data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot bootstrap class for the ms-core-data microservice.
 *
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
@SpringBootApplication
public class MsCoreDataApplication {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args startup arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsCoreDataApplication.class, args);
	}

}
