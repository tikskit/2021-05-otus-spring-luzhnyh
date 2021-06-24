package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;

import java.io.PrintStream;

@Service
public class GameResultOutputConsole implements GameResultOutput{

    public static final String GAME_RESULT_CODE = "game_result";

    public final PrintStream out;
    private final ComfyLocalizer localizer;

    public GameResultOutputConsole(@Value("#{T(java.lang.System).out}") PrintStream out, ComfyLocalizer localizer) {
        this.out = out;
        this.localizer = localizer;
    }

    @Override
    public void showResult(PlayerInfo playerInfo, GameResult gameResult) {
        out.print(localizer.getMessage(GAME_RESULT_CODE, playerInfo, gameResult.correctAnswerCount(),
                gameResult.questionsCount()));
    }
}
