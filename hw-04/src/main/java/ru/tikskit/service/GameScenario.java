package ru.tikskit.service;

import ru.tikskit.domain.GameResult;


/**
 * Интерфейс функционала тестирования по тем или иным правилам. Каждая реализация представляет конкретные правила
 * проведения тестирования.
 */
interface GameScenario {
    GameResult askQuestions();
}
