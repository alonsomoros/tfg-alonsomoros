package com.alonsomoros.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RecurringEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecurringEngineApplication.class, args);
	}

}
