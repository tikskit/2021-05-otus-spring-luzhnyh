package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;

import java.io.PrintStream;

@Service
public class GameResultOutputConsole implements GameResultOutput{

    public static final String MESSAGE_TEMPLATE = "%s scores %d points out of %d";

    public final PrintStream out;

    public GameResultOutputConsole(@Value("#{T(java.lang.System).out}") PrintStream out) {
        this.out = out;
    }

    @Override
    public void showResult(PlayerInfo playerInfo, GameResult gameResult) {
        out.print(String.format(MESSAGE_TEMPLATE, playerInfo, gameResult.correctAnswerCount(),
                gameResult.questionsCount()));
    }
}
