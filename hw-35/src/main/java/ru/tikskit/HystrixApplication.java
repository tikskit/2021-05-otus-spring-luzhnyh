package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCircuitBreaker
public class HystrixApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HystrixApplication.class, args);
	}

}

