package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Тесты для класса QuestionAnswerRequestConsole")
public class QuestionAnswerRequestConsoleTest {
    private BufferedReader reader;
    private PrintStream out;
    private QuestionAnswerRequest questionAnswerRequest;

    @BeforeEach
    public void setUp() {
        reader = mock(BufferedReader.class);
        out = mock(PrintStream.class);

        questionAnswerRequest = new QuestionAnswerRequestConsole(reader, out);
    }

    @DisplayName("Проверка вывода приглашающего сообщения")
    @Test
    public void checkRequestMessage() throws IOException {
        when(reader.readLine()).thenReturn("1");

        questionAnswerRequest.requestAnswerNo(1);
        verify(out).print(QuestionAnswerRequestConsole.REQUEST_MESSAGE);
    }

    @DisplayName("Проверяем, что класс обрабатывает ввод пользователя и возвращает корректный результат")
    @Test
    public void checkValidUserAnswer() throws IOException {
        when(reader.readLine()).thenReturn("1");


        int optionIndex = questionAnswerRequest.requestAnswerNo(1);
        assertThat(optionIndex).as("проверка индекса варианта ответа").isEqualTo(0);
    }

    @DisplayName("Проверяем обработку некорректного ввода пользователя")
    @Test
    public void checkInvalidUserAnswers() throws IOException {
        when(reader.readLine()).thenReturn("whut?", "", "0", "2", "1");


        int optionIndex = questionAnswerRequest.requestAnswerNo(1);
        assertThat(optionIndex).as("проверка индекса варианта ответа").isEqualTo(0);

        verify(out, times(1)).println(String.format(QuestionAnswerRequestConsole.WRONG_INPUT_TEMPLATE, "whut?"));
        verify(out, times(1)).println(String.format(QuestionAnswerRequestConsole.WRONG_INPUT_TEMPLATE, "0"));
        verify(out, times(1)).println(String.format(QuestionAnswerRequestConsole.WRONG_INPUT_TEMPLATE, "2"));
    }
}
