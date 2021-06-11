package ru.tikskit.service;


import ru.tikskit.domain.Question;

/**
 * Интерфейс, описывающий взаимодействие с пользователем.
 */
interface QuestionIO {
    int askQuestion(Question question);
}
