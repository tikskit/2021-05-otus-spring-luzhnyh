package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.Question;

import java.util.List;

@Service
public class GameDataProviderImpl implements GameDataProvider{
    private final QuestionsFileReader questionsFileReader;

    private List<Question> questions;

    public GameDataProviderImpl(QuestionsFileReader questionsFileReader) {
        this.questionsFileReader = questionsFileReader;
    }

    @Override
    public List<Question> getQuestions() {
        if (questions == null) {
            try {
                questions = questionsFileReader.loadQuestions();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return questions;
    }

}
