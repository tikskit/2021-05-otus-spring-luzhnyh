package ru.tikskit.service;

import ru.tikskit.domain.Question;

import java.util.List;

public interface GameDataProvider {
    List<Question> getQuestions();
}
