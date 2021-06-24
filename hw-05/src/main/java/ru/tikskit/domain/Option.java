package ru.tikskit.domain;

import java.util.Objects;

/**
 * Вариант ответа
 */
public class Option {
    private final String text;

    public Option(String text) {
        Objects.requireNonNull(text);
        if (text.trim().equals("")) {
            throw new IllegalArgumentException("Empty option");
        }

        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * Проверка, что переданный в строке ответ совпадает со значением данного объекта
     * @param value проверяемое значение ответа
     * @return @true если проверяемое значение и значение объекта совпадают
     */
    public boolean matchesValue(Option value) {
        return text.equalsIgnoreCase(value.getText().trim());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return matchesValue(option);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
