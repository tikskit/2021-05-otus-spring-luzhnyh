package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

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

    }

    private PlayerInfo askPlayerInfo() {
        System.out.print("Enter user name: ");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();

        System.out.print("Enter user surname: ");
        String surname = s.nextLine();


        return new PlayerInfo(name, surname);
    }
}
