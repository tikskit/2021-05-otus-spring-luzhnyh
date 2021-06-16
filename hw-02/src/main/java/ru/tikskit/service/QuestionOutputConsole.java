package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintStream;


@Service
public class QuestionOutputConsole implements QuestionOutput{
    public static final String OPTION_FORMAT = "%d. %s";

    private final PrintStream out;

    public QuestionOutputConsole(@Value("#{T(java.lang.System).out}")PrintStream out) {
        this.out = out;
    }

    @Override
    public void printQuestion(String questionText, String[] options) {
        out.println(questionText);
        for (int i = 0; i < options.length; i++) {
            out.println(String.format(OPTION_FORMAT, i + 1, options[i]));
        }
    }
}
