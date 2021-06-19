package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.QuestionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Сценарий игры, при котором каждый вопрос задаются в рандомном порядке по одному разу. Игра заканчивается,
 * когда все вопросы заданы.
 */

@Service
class GameScenarioShuffled implements GameScenario {

    private final GameDataProvider gameDataProvider;
    private final QuestionOutput questionOutput;
    private final QuestionAnswerRequest questionAnswerRequest;

    public GameScenarioShuffled(GameDataProvider gameDataProvider, QuestionOutput questionOutput,
                                QuestionAnswerRequest questionAnswerRequest) {
        this.gameDataProvider = gameDataProvider;
        this.questionOutput = questionOutput;
        this.questionAnswerRequest = questionAnswerRequest;
    }

    @Override
    public GameResult askQuestions() {
        List<Question> questions = new ArrayList<>(gameDataProvider.getQuestions());
        Collections.shuffle(questions);

        GameResult gameResult = new GameResult();
        for (Question question : questions) {
            gameResult.addResult(askQuestion(question));
        }

        return gameResult;
    }


    private QuestionResult askQuestion(Question question) {
        List<Option> shuffledOptions = new ArrayList<>(question.getOptions());
        Collections.shuffle(shuffledOptions);

        String[] shuffledOptionTexts = shuffledOptions.stream().map(Option::getText).toArray(String[]::new);
        questionOutput.printQuestion(question.getText(), shuffledOptionTexts);
        int index = questionAnswerRequest.requestAnswerNo(shuffledOptionTexts.length);

        return new QuestionResult(question, shuffledOptions.get(index));
    }
}
