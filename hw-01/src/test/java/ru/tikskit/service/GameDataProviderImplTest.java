package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Тесты для класса GameProcessorImpl")
public class GameDataProviderImplTest {

    @DisplayName("Проверка создания экземпляра класса из CVS файла")
    @Test
    public void instantiateTest() {
        assertThatNoException().isThrownBy(() -> new GameDataProviderImpl("/instantiateTest.csv"));
    }

    @DisplayName("Проверка, что класс правильно загружает вопросы из файла")
    @Test
    public void checkQuestions() throws Exception {
        GameDataProvider gdp = new GameDataProviderImpl("/checkQuestions.csv");
        assertThat(gdp.getQuestions()).size().isEqualTo(2);
        assertThat(gdp.getQuestions().get(0).getText()).isEqualTo("question 1");
        assertThat(gdp.getQuestions().get(1).getText()).isEqualTo("question 2");
    }
}
