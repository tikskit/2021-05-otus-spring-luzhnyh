package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.PlayerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;


@Service
class PlayerInfoReceiverConsole implements PlayerInfoReceiver{

    public static final String ENTER_PLAYER_NAME_CODE = "enter_player_name";
    public static final String ENTER_PLAYER_SURNAME_CODE = "enter_player_surname";

    private final BufferedReader reader;
    public final PrintStream out;
    private final ComfyLocalizer localizer;

    public PlayerInfoReceiverConsole(
            @Value("#{new java.io.BufferedReader(new java.io.InputStreamReader(T(java.lang.System).in))}")
                    BufferedReader reader,
            @Value("#{T(java.lang.System).out}") PrintStream out,
            ComfyLocalizer localizer) {
        this.reader = reader;
        this.out = out;
        this.localizer = localizer;
    }

    @Override
    public PlayerInfo get() {

        String name = null;
        String surname = null;
        try {
            out.print(localizer.getMessage(ENTER_PLAYER_NAME_CODE) + ": ");
            name = reader.readLine();

            out.print(localizer.getMessage(ENTER_PLAYER_SURNAME_CODE) + ": ");
            surname = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new PlayerInfo(name, surname);
    }
}
