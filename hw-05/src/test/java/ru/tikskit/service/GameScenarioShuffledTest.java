package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.tikskit.config.QuestionsConfig;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameScenarioShuffledTest {

    @MockBean
    private BufferedReader reader;
    private GameScenario gameScenario;
    @MockBean
    private PrintStream out;
    @MockBean
    GameDataProvider gameDataProvider;
    @MockBean
    QuestionOutput questionOutput;
    @MockBean
    QuestionAnswerRequest questionAnswerRequest;

    @Autowired
    private QuestionsConfig questionsConfig;
    @Autowired
    private ComfyLocalizer localizer;

    @Test
    public void checkQuestionsConfig() {
        assertThat(questionsConfig).as("check questionsConfig is inited").isNotNull();
    }

    @Test
    public void checkLocalizer() {
        assertThat(localizer).as("check localizer is inited").isNotNull();
    }


    @BeforeEach
    public void setUp() throws IOException {
        when(reader.readLine()).thenReturn("1", "1");

        QuestionsFileReader questionsFileReader = new QuestionsFileReaderImpl(questionsConfig);
        GameDataProvider gameDataProvider = new GameDataProviderImpl(questionsFileReader);
        QuestionOutput questionOutput = new QuestionOutputConsole(out);
        QuestionAnswerRequest questionAnswerRequest = new QuestionAnswerRequestConsole(reader, out, localizer);
        gameScenario = new GameScenarioShuffled(gameDataProvider, questionOutput, questionAnswerRequest, localizer);
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
        QuestionAnswerRequest questionAnswerRequest = new QuestionAnswerRequestConsole(reader, out, localizer);
        GameScenario gameScenario = new GameScenarioShuffled(gameDataProvider, questionOutput, questionAnswerRequest,
                localizer);

        List<Question> questionsFake = createQuestionsFake(localizer);
        when(gameDataProvider.getQuestions()).thenReturn(questionsFake);


        gameScenario.askQuestions();

        for (Question question : questionsFake) {
            verify(questionOutput, times(questionsFake.size())).printQuestion(anyString(), any(String[].class));

        }
    }

    @DisplayName("Проверка использования методов QuestionAnswerRequest")
    @Test
    public void checkQuestionAnswerRequest() {
        QuestionOutput questionOutput = new QuestionOutputConsole(out);
        GameScenario gameScenario = new GameScenarioShuffled(gameDataProvider, questionOutput, questionAnswerRequest,
                localizer);

        List<Question> questionsFake = createQuestionsFake(localizer);
        when(gameDataProvider.getQuestions()).thenReturn(questionsFake);


        gameScenario.askQuestions();

        verify(questionAnswerRequest, atLeastOnce()).requestAnswerNo(anyInt());
    }

    private static List<Question> createQuestionsFake(ComfyLocalizer localizer) {
        List<Question> res = new ArrayList<>();

        res.add(createQuestionFake("Question1", 0, localizer, "Option1.1",
                "Option1.2"));
        res.add(createQuestionFake("Question2", 1, localizer, "Option2.1",
                "Option2.2"));

        return res;
    }

    private static Question createQuestionFake(String questionText, int correctOptionNo, ComfyLocalizer localizer,
                                               String... options) {
        Objects.checkIndex(correctOptionNo, options.length);

        List<Option> optionList = Arrays.stream(options).
                map(localizer::getMessage).
                map(Option::new).
                collect(Collectors.toList());
        return new Question(localizer.getMessage(questionText), optionList, optionList.get(correctOptionNo));
    }
}
