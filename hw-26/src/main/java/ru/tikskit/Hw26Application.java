package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.tikskit.service.TransferService;
import ru.tikskit.service.TransferServiceImpl;

import java.io.IOException;

@EnableMongoRepositories
@SpringBootApplication
public class Hw26Application {

	public static void main(String[] args) throws IOException {

		ConfigurableApplicationContext context = SpringApplication.run(Hw26Application.class, args);


		TransferService transferService = context.getBean(TransferServiceImpl.class);
		transferService.start();

	}



}
