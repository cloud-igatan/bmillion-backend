package com.example.bmillion_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BmillionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BmillionBackendApplication.class, args);
	}

}
