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
 * Сценарий игры, при котором вопросы просто задаются в рандомном порядке по одному разу. Игра заканчивается,
 * когда все вопросы заданы.
 */

@Service
class GameScenarioShuffled implements GameScenario {

    private final GameDataProvider gameDataProvider;
    private final QuestionIO questionIO;

    public GameScenarioShuffled(GameDataProvider gameDataProvider, QuestionIO questionIO) {
        this.gameDataProvider = gameDataProvider;
        this.questionIO = questionIO;
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
        int index = questionIO.askQuestion(question.getText(), shuffledOptionTexts);

        return new QuestionResult(question, shuffledOptions.get(index));
    }
}
