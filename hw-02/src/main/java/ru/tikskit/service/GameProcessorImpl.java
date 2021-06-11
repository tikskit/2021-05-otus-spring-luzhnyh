package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;
import ru.tikskit.domain.QuestionResult;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;


@Service
public class GameProcessorImpl implements GameProcessor {
    private final GameScenario gameScenario;
    private final InputStream in;
    private final PrintStream out;

    public GameProcessorImpl(GameScenario gameScenario,
                             @Value("#{T(java.lang.System).in}") InputStream in,
                             @Value("#{T(java.lang.System).out}") PrintStream out) {
        this.gameScenario = gameScenario;
        this.in = in;
        this.out = out;
    }

    @Override
    public void play() {
        PlayerInfo pi = askPlayerInfo();

        GameResult gameResult = gameScenario.askQuestions();
        printResult(pi, gameResult);
    }

    private PlayerInfo askPlayerInfo() {
        out.print("Enter user name: ");
        Scanner s = new Scanner(in);
        String name = s.nextLine();

        out.print("Enter user surname: ");
        String surname = s.nextLine();


        return new PlayerInfo(name, surname);
    }

    private void printResult(PlayerInfo pi, GameResult gameResult) {
        long score = gameResult.getResults().stream().map(QuestionResult::isCorrect).filter(m -> m).count();
        out.println(String.format("%s scores %d out of %d points", pi, score, gameResult.getResults().size()));
    }
}
