package ru.tikskit.service;

import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

public class GameProcessorImpl implements GameProcessor {
    private final GameDataProvider gameDataProvider;

    public GameProcessorImpl(GameDataProvider gameDataProvider) {
        this.gameDataProvider = gameDataProvider;
    }

    @Override
    public void play() {
        for (Question q: gameDataProvider.getQuestions()) {
            System.out.println(String.format("Question: %s", q.getText()));
            for (Option o : q.getOptions()) {
                System.out.println(String.format("\tOption: %s", o.getText()));

            }
        }
    }
}
