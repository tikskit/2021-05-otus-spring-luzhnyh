package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тесты для класса QuestionAnswerRequestConsole")
@SpringBootTest
public class QuestionAnswerRequestConsoleTest {
    private BufferedReader reader;
    private PrintStream out;
    private QuestionAnswerRequest questionAnswerRequest;

    @Autowired
    private ComfyLocalizer localizer;

    @BeforeEach
    public void setUp() {
        reader = mock(BufferedReader.class);
        out = mock(PrintStream.class);

        questionAnswerRequest = new QuestionAnswerRequestConsole(reader, out, localizer);
    }

    @Test
    public void checkLocalizer() {
        assertThat(localizer).as("check localizer is inited").isNotNull();
    }

    @DisplayName("Проверка вывода приглашающего сообщения")
    @Test
    public void checkRequestMessage() throws IOException {
        when(reader.readLine()).thenReturn("1");

        questionAnswerRequest.requestAnswerNo(1);

        verify(out).print(localizer.getMessage(QuestionAnswerRequestConsole.REQUEST_MESSAGE_CODE) + " ");
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

        verify(out, times(1)).println(
                localizer.getMessage(QuestionAnswerRequestConsole.INCORRECT_INPUT_CODE, "whut?"));
        verify(out, times(1)).println(
                localizer.getMessage(QuestionAnswerRequestConsole.INCORRECT_INPUT_CODE, "0"));
        verify(out, times(1)).println(
                localizer.getMessage(QuestionAnswerRequestConsole.INCORRECT_INPUT_CODE, "2"));
    }
}
