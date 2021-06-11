package ru.tikskit.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class QuestionIOConsoleTest {
    private BufferedReader reader;

    @BeforeEach
    public void setUp() {
        reader = mock(BufferedReader.class);
    }

    @DisplayName("Проверяем, что пользовательский выбор начинается с 1, а индексы вариантов с 0")
    @Test
    public void checkUserInputCorrespondsToOptionIndex() throws Exception{

        when(reader.readLine()).thenReturn("1");

        QuestionIO questionIO = new QuestionIOConsole(reader);
        int answer = questionIO.askQuestion("question", new String[]{"Option 1", "Option 2", "Option 3"});
        assertThat(answer).isEqualTo(0);
    }

    @DisplayName("Проверяем, что запрос значения будет несколько раз, до тех пор, пока пользователь не введет корректное значение")
    @Test
    public void checkCorrectUserInput() throws Exception{

        when(reader.readLine()).thenReturn("", "-1", "4", "1");

        QuestionIO questionIO = new QuestionIOConsole(reader);
        questionIO.askQuestion("question", new String[]{"Option 1", "Option 2", "Option 3"});

        verify(reader, times(4)).readLine();
    }

}
