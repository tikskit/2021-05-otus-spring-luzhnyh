package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthorizationApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AuthorizationApplication.class, args);
		PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

		System.out.println("Manager's pass: " + passwordEncoder.encode("Manager"));
		System.out.println("Supporter's pass: " + passwordEncoder.encode("Supporter"));
		System.out.println("Reviewer's pass: " + passwordEncoder.encode("Reviewer"));
	}

}
