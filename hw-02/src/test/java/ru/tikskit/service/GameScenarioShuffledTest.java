package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

public class GameScenarioShuffledTest {

    public static final String QUESTIONS_FILE_PATH = "questions.file.path";
    private BufferedReader reader;
    private GameScenario gameScenario;
    private PrintStream out;

    @BeforeEach
    public void setUp() throws IOException {
        reader = mock(BufferedReader.class);
        out = mock(PrintStream.class);

        when(reader.readLine()).thenReturn("1", "1");

        Properties props = getProperties();


        QuestionsFileReader questionsFileReader = new QuestionsFileReaderImpl((String)props.get(QUESTIONS_FILE_PATH));
        GameDataProvider gameDataProvider = new GameDataProviderImpl(questionsFileReader);
        QuestionOutput questionOutput = new QuestionOutputConsole(out);
        QuestionAnswerRequest questionAnswerRequest = new QuestionAnswerRequestConsole(reader, out);
        gameScenario = new GameScenarioShuffled(gameDataProvider, questionOutput, questionAnswerRequest);
    }

    @DisplayName("Проверяем, что количество результатов соответствует количеству вопросов")
    @Test
    public void checkResultCount() {
        GameResult gameResult = gameScenario.askQuestions();
        assertThat(gameResult.getResults().size()).isEqualTo(2);
    }

    @DisplayName("Проверка использования методов QuestionOutput")
    @Test
    public void checkQuestionOutput() {
        GameDataProvider gameDataProvider = mock(GameDataProviderImpl.class);
        QuestionOutput questionOutput = mock(QuestionOutputConsole.class);
        QuestionAnswerRequest questionAnswerRequest = new QuestionAnswerRequestConsole(reader, out);
        GameScenario gameScenario = new GameScenarioShuffled(gameDataProvider, questionOutput, questionAnswerRequest);

        List<Question> questionsFake = createQuestionsFake();
        when(gameDataProvider.getQuestions()).thenReturn(questionsFake);


        gameScenario.askQuestions();

        for (Question question : questionsFake) {
            verify(questionOutput, times(questionsFake.size())).printQuestion(anyString(), any(String[].class));

        }
    }

    @DisplayName("Проверка использования методов QuestionAnswerRequest")
    @Test
    public void checkQuestionAnswerRequest() {
        GameDataProvider gameDataProvider = mock(GameDataProviderImpl.class);
        QuestionOutput questionOutput = new QuestionOutputConsole(out);
        QuestionAnswerRequest questionAnswerRequest = mock(QuestionAnswerRequestConsole.class);
        GameScenario gameScenario = new GameScenarioShuffled(gameDataProvider, questionOutput, questionAnswerRequest);

        List<Question> questionsFake = createQuestionsFake();
        when(gameDataProvider.getQuestions()).thenReturn(questionsFake);


        gameScenario.askQuestions();

        verify(questionAnswerRequest, atLeastOnce()).requestAnswerNo(anyInt());
    }

    private static List<Question> createQuestionsFake() {
        List<Question> res = new ArrayList<>();
        res.add(createQuestionFake("Question1", 0, "Option1.1", "Option1.2"));
        res.add(createQuestionFake("Question2", 1, "Option2.1", "Option2.2"));

        return res;
    }

    private static Question createQuestionFake(String questionText, int correctOptionNo, String... options) {
        Objects.checkIndex(correctOptionNo, options.length);

        List<Option> optionList = Arrays.stream(options).map(Option::new).collect(Collectors.toList());
        return new Question(questionText, optionList, optionList.get(correctOptionNo));
    }


    private Properties getProperties() {
        Properties props = new Properties();
        props.put(QUESTIONS_FILE_PATH, "/questions.csv");
        return props;
    }
}
