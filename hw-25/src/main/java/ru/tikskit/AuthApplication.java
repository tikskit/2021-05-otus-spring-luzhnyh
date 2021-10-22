package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// url h2 консоли: http://localhost:8080/h2-console
// url базы: jdbc:h2:mem:librarydb

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
