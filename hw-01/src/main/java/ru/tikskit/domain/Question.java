package ru.tikskit.domain;

import java.util.List;

/**
 * Вопрос
 */
public class Question {
    private final String text;
    private final List<Option> options;

    public Question(String text, List<Option> options) {
        this.text = text;
        this.options = options;
    }

    public String getText() {
        return text;
    }

    public List<Option> getOptions() {
        return options;
    }
}
