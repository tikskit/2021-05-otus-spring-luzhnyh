package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;

import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Проверяем работу класса, отвечающего за вывод результата игры")
public class GameResultOutputConsoleTest {
    private static final String PLAYER_NAME = "Yury";
    private static final String PLAYER_SURNAME = "Gagarin";

    @DisplayName("Тест проверяет выводимое по окончании игры сообщение")
    @Test
    public void checkResultGameMessage() {
        PrintStream out = mock(PrintStream.class);
        PlayerInfo pi = new PlayerInfo(PLAYER_NAME, PLAYER_SURNAME);
        GameResult gameResult = mock(GameResult.class);

        final int CORRECT_ANSWERS_COUNT = 3;
        final int QUESTIONS_COUNT = 5;

        when(gameResult.correctAnswerCount()).thenReturn(CORRECT_ANSWERS_COUNT);
        when(gameResult.questionsCount()).thenReturn(QUESTIONS_COUNT);

        GameResultOutput gameResultOutput = new GameResultOutputConsole(out);
        gameResultOutput.showResult(pi, gameResult);

        verify(out).print(String.format(GameResultOutputConsole.MESSAGE_TEMPLATE, PLAYER_NAME + " " + PLAYER_SURNAME,
                CORRECT_ANSWERS_COUNT, QUESTIONS_COUNT));
    }
}
