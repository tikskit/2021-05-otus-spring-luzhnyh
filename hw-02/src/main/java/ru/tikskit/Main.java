package ru.tikskit;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.tikskit.service.GameProcessor;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        GameProcessor gameProcessor = context.getBean("gameProcessor", GameProcessor.class);
        gameProcessor.play();
    }
}
