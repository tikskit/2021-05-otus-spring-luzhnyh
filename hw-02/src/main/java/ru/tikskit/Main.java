package ru.tikskit;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.tikskit.service.GameProcessor;

@Configuration
@ComponentScan
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        GameProcessor gameProcessor = context.getBean(GameProcessor.class);
        gameProcessor.play();
    }
}
