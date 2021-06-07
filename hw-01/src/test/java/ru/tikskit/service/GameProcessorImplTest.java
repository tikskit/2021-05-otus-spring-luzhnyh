package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@DisplayName("Тесты для класса GameProcessorImpl")
public class GameProcessorImplTest {
    @DisplayName("Проверим, что GameProcessor использует GameDataProvider")
    @Test
    public void testQuestionsAreGettingAsked() {
        GameDataProvider gameDataProvider = mock(GameDataProviderImpl.class);
        GameProcessor gameProcessor = new GameProcessorImpl(gameDataProvider);
        gameProcessor.play();
        verify(gameDataProvider, times(1)).getQuestions();
    }
}
