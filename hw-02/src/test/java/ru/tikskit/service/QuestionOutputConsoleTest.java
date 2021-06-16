package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("Тесты для класса QuestionOutputConsole")
public class QuestionOutputConsoleTest {
    private PrintStream out;
    private QuestionOutputConsole questionOutputConsole;


    @BeforeEach
    public void setUp() {
        out = mock(PrintStream.class);
        questionOutputConsole = new QuestionOutputConsole(out);
    }

    @DisplayName("Проверка содержимого вывода в консоль")
    @Test
    public void checkPrintQuestion() {
        final String questionText = "Question";
        final String[] options = {"Option1", "Option2", "Option3"};
        questionOutputConsole.printQuestion(questionText, options);

        verify(out).println(questionText);
        for (int i = 0; i < options.length; i++) {
            verify(out).println(String.format(QuestionOutputConsole.OPTION_FORMAT, i + 1, options[i]));
        }
    }
}
