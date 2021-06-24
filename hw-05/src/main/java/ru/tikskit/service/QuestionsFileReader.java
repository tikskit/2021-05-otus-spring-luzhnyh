package ru.tikskit.service;

import ru.tikskit.domain.Question;

import java.util.List;

interface QuestionsFileReader {
    List<Question> loadQuestions() throws Exception;
}
