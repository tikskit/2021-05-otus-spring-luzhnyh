package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.PlayerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;


@Service
class PlayerInfoReceiverConsole implements PlayerInfoReceiver{

    private final BufferedReader reader;
    public final PrintStream out;

    public PlayerInfoReceiverConsole(
            @Value("#{new java.io.BufferedReader(new java.io.InputStreamReader(T(java.lang.System).in))}")
                    BufferedReader reader,
            @Value("#{T(java.lang.System).out}") PrintStream out) {
        this.reader = reader;
        this.out = out;
    }

    @Override
    public PlayerInfo get() {

        String name = null;
        String surname = null;
        try {
            out.print("Enter player name: ");
            name = reader.readLine();

            out.print("Enter player surname: ");
            surname = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new PlayerInfo(name, surname);
    }
}
