package ru.tikskit.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class GameResult {
    private final List<QuestionResult> results = new ArrayList<>();
    private List<QuestionResult> exposedResults;

    public void addResult(QuestionResult result) {
        Objects.requireNonNull(result);

        if (questionPresents(result)) {
            throw new IllegalArgumentException("The question result is in the list already");
        }

        results.add(result);
        exposedResults = null;
    }

    public List<QuestionResult> getResults() {
        if (exposedResults == null) {
            exposedResults = List.copyOf(results);
        }
        return exposedResults;
    }

    private boolean questionPresents(QuestionResult result) {
        Objects.requireNonNull(result);

        for (QuestionResult qr : results ) {
            if (qr.getQuestion().equals(result.getQuestion())) {
                return true;
            }
        }

        return false;
    }

    public int questionsCount() {
        return results.size();
    }

    public int correctAnswerCount() {
        return (int) results.stream().filter(QuestionResult::isCorrect).count();
    }

}
