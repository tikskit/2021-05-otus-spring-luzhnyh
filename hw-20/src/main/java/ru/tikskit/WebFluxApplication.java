package ru.tikskit;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories
@EnableReactiveMongoRepositories
@EnableMongock
public class WebFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}

}
