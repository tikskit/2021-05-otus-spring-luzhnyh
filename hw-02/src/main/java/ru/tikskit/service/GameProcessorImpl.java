package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;

@Service
public class GameProcessorImpl implements GameProcessor {
    private final GameScenario gameScenario;
    private final PlayerInfoReceiver playerInfoReceiver;
    private final GameResultOutput gameResultOutput;

    public GameProcessorImpl(GameScenario gameScenario, PlayerInfoReceiver playerInfoReceiver,
                             GameResultOutput gameResultOutput) {
        this.gameScenario = gameScenario;
        this.playerInfoReceiver = playerInfoReceiver;
        this.gameResultOutput = gameResultOutput;
    }

    @Override
    public void play() {
        PlayerInfo pi = playerInfoReceiver.get();

        GameResult gameResult = gameScenario.askQuestions();
        gameResultOutput.showResult(pi, gameResult);
    }
}
