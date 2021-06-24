package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;

@Service
public class GameProcessorImpl implements GameProcessor {
    private final GameScenario gameScenario;
    private final GameResultOutput gameResultOutput;
    private final LoginedPlayedInfo loginedPlayedInfo;

    public GameProcessorImpl(GameScenario gameScenario, LoginedPlayedInfo loginedPlayedInfo,
                             GameResultOutput gameResultOutput) {
        this.gameScenario = gameScenario;
        this.loginedPlayedInfo = loginedPlayedInfo;
        this.gameResultOutput = gameResultOutput;
    }

    @Override
    public boolean play() {
        if (loginedPlayedInfo.isPlayerLogined()) {
            GameResult gameResult = gameScenario.askQuestions();
            gameResultOutput.showResult(loginedPlayedInfo.getPlayerInfo(), gameResult);
            return true;
        } else {
            return false;
        }
    }
}
