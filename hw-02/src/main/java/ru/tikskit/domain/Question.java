package ru.tikskit.domain;

import java.util.List;
import java.util.Objects;

/**
 * Вопрос
 */
public class Question {
    private final String text;
    private final List<Option> options;
    private final int correctOptionIndex;


    public Question(String text, List<Option> options, int correctOptionIndex) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(options);
        Objects.checkIndex(correctOptionIndex, options.size());

        if (text.trim().equals("")) {
            throw new IllegalArgumentException("Empty question text");
        }
        if (options.size() == 0) {
            throw new IllegalArgumentException("Empty options list");
        }

        this.text = text;
        this.options = List.copyOf(options); // Чтобы изменение списка в вызывающем коде не повлияло на согласованность объекта
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getText() {
        return text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean isCorrectAnswer(Option test) {
        return options.get(correctOptionIndex).matchesValue(test);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return correctOptionIndex == question.correctOptionIndex &&
                text.equals(question.text) &&
                options.equals(question.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, options, correctOptionIndex);
    }
}
