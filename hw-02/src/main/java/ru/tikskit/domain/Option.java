package ru.tikskit.domain;

/**
 * Вариант ответа
 */
public class Option {
    private final String text;

    public Option(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
