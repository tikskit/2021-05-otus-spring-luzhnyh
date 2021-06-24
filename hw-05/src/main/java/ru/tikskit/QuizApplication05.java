package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.tikskit.service.GameProcessor;

@SpringBootApplication
public class QuizApplication05 {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication05.class, args);
//		ApplicationContext context = SpringApplication.run(QuizApplication05.class, args);
//		GameProcessor gameProcessor = context.getBean(GameProcessor.class);
//		gameProcessor.play();
	}
}