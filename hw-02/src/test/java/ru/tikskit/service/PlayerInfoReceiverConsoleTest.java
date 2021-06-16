package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tikskit.domain.PlayerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Проверяем работу класса, получающего имя пользователя из консоли")
public class PlayerInfoReceiverConsoleTest {
    private static final String PLAYER_NAME = "Yury";
    private static final String PLAYER_SURNAME = "Gagarin";


    private PrintStream out;
    BufferedReader reader;
    PlayerInfoReceiver playerInfoReceiver;


    @BeforeEach
    public void setUp() {
        out = mock(PrintStream.class);
        reader = mock(BufferedReader.class);
        playerInfoReceiver = new PlayerInfoReceiverConsole(reader, out);
    }

    @DisplayName("Проверяем корректность вывода текста пользователю")
    @Test
    public void checkOutput() {
        playerInfoReceiver.get();


        verify(out, times(2)).print(anyString());

        verify(out, times(1)).print("Enter player name: ");
        verify(out, times(1)).print("Enter player surname: ");
    }

    @DisplayName("Проверяем, что возвращаемый объект имеет корректные данные")
    @Test
    public void checkPlayerInfoRequest() throws IOException {

        when(reader.readLine()).thenReturn(PLAYER_NAME, PLAYER_SURNAME);

        PlayerInfo playerInfo = playerInfoReceiver.get();

        assertThat(playerInfo).isNotNull();
        assertThat(playerInfo.getName()).as("Проверяем имя")
                .isEqualTo(PLAYER_NAME);
        assertThat(playerInfo.getSurname()).as("Проверяем фамилию")
                .isEqualTo(PLAYER_SURNAME);

    }
}
