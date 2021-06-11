package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;
import ru.tikskit.domain.QuestionResult;

import java.util.Scanner;


@Service
public class GameProcessorImpl implements GameProcessor {
    private final GameScenario gameScenario;

    public GameProcessorImpl(GameScenario gameScenario) {
        this.gameScenario = gameScenario;
    }

    @Override
    public void play() {
        PlayerInfo pi = askPlayerInfo();

        GameResult gameResult = gameScenario.askQuestions();
        printResult(pi, gameResult);
    }

    private PlayerInfo askPlayerInfo() {
        System.out.print("Enter player name: ");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();

        System.out.print("Enter player surname: ");
        String surname = s.nextLine();


        return new PlayerInfo(name, surname);
    }

    private void printResult(PlayerInfo pi, GameResult gameResult) {
        long score = gameResult.getResults().stream().map(QuestionResult::isCorrect).filter(m -> m).count();
        System.out.println(String.format("%s scores %d out of %d points", pi, score, gameResult.getResults().size()));
    }
}
