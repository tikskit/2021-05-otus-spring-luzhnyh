package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.tikskit.domain.PlayerInfo;
import ru.tikskit.service.ComfyLocalizer;
import ru.tikskit.service.GameProcessor;
import ru.tikskit.service.LoginedPlayedInfo;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {
    private final static String PlAYER_NOT_LOGINED_REASON_CODE = "player_not_logined_reason";

    private final GameProcessor gameProcessor;
    private final LoginedPlayedInfo loginedPlayedInfo;
    private final ComfyLocalizer localizer;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login(@ShellOption(help = "Player's name") String name,
                            @ShellOption(help = "Player's surname") String surname) {
        loginedPlayedInfo.setPlayerInfo(new PlayerInfo(name, surname));
    }

    @ShellMethod(value = "Start command", key = {"p", "play"})
    @ShellMethodAvailability(value = "isPlayAvailable")
    public void play() {
        gameProcessor.play();
    }

    private Availability isPlayAvailable() {
        return loginedPlayedInfo.isPlayerLogined() ?
                Availability.available() :
                Availability.unavailable(localizer.getMessage(PlAYER_NOT_LOGINED_REASON_CODE));
    }

}
