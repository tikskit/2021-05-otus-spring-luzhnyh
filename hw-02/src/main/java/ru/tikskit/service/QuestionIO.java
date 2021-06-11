package ru.tikskit.service;


import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

/**
 * Интерфейс, описывающий взаимодействие с пользователем.
 */
interface QuestionIO {
    /**
     * Выводит пользователю текст вопроса и варианты ответа (в порядке, в котором они переданы в метод).
     * Возвращает выбранный пользователем вариант.
     *
     * @param questionText Текст вопроса
     * @param options Массив с текстами вариантов ответа
     * @return Возвращает индекс выбранного пользователем варианта в массиве options. Гарантируется, что значения этого
     * индекса соответствует границам options.
     */
    int askQuestion(String questionText, String[] options);
}
