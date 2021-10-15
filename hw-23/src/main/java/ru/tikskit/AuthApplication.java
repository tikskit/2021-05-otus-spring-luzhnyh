package ru.tikskit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

// url h2 консоли: http://localhost:8080/h2-console
// url базы: jdbc:h2:mem:librarydb

@SpringBootApplication
public class AuthApplication {
/*
	@Autowired
	PasswordEncoder passwordEncoder;
*/

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}


/*	@PostConstruct
	private void postConstruct() {
		String encPass = passwordEncoder.encode("password");
		System.out.println(encPass);
	}*/
}
