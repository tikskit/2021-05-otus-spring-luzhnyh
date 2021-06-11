package ru.tikskit.domain;

import java.util.Objects;

public class QuestionResult {
    private final Question question;
    private final Option chosen;
    private final boolean isCorrect;

    public QuestionResult(Question question, Option chosen) {
        Objects.requireNonNull(question, "Question can't be null");
        Objects.requireNonNull(chosen, "Option can't be null");

        if (!question.getOptions().contains(chosen)) {
            throw new IllegalArgumentException("Option is\'t from the list");
        }

        this.question = question;
        this.chosen = chosen;
        this.isCorrect = question.isCorrectAnswer(chosen);
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public Option getChosen() {
        return chosen;
    }
}
