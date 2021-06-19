package ru.tikskit.domain;

import java.util.List;
import java.util.Objects;

/**
 * Вопрос
 */
public class Question {
    private final String text;
    private final List<Option> options;
    private final Option correctOption;


    public Question(String text, List<Option> options, Option correctOption) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(options);
        Objects.requireNonNull(correctOption);

        if (text.trim().equals("")) {
            throw new IllegalArgumentException("Empty question text");
        }
        if (options.size() == 0) {
            throw new IllegalArgumentException("Empty options list");
        }

        if (!options.contains(correctOption)) {
            throw new IllegalArgumentException("The correct option is not in the options list");
        }

        this.text = text;
        this.options = List.copyOf(options); // Чтобы изменение списка в вызывающем коде не повлияло на согласованность объекта
        this.correctOption = correctOption;
    }

    public String getText() {
        return text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean isCorrectAnswer(Option test) {
        return correctOption.matchesValue(test);
    }

}
