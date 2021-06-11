package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tikskit.domain.GameResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

public class GameScenarioShuffledTest {

    public static final String QUESTIONS_FILE_PATH = "questions.file.path";
    private BufferedReader reader;
    private GameScenario gameScenario;

    @BeforeEach
    public void setUp() throws IOException {
        reader = mock(BufferedReader.class);

        when(reader.readLine()).thenReturn("1", "1");

        Properties props = getProperties();


        GameDataProvider gameDataProvider = new GameDataProviderImpl((String)props.get(QUESTIONS_FILE_PATH));
        QuestionIO questionIO = new QuestionIOConsole(reader);
        gameScenario = new GameScenarioShuffled(gameDataProvider, questionIO);
    }

    @DisplayName("Проверяем, что количество результатов соответствует количеству вопросов")
    @Test
    public void checkResultCount() {
        GameResult gameResult = gameScenario.askQuestions();
        assertThat(gameResult.getResults().size()).isEqualTo(2);
    }


    private Properties getProperties() {
        Properties props = new Properties();
        props.put(QUESTIONS_FILE_PATH, "/questions.csv");
        return props;
    }
}
