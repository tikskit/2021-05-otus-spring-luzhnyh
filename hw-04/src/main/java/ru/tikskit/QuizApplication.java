package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.tikskit.service.GameProcessor;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(QuizApplication.class, args);
		GameProcessor gameProcessor = context.getBean(GameProcessor.class);
		gameProcessor.play();
	}
}