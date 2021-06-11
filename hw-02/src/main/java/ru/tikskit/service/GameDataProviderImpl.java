package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:/application.properties")
public class GameDataProviderImpl implements GameDataProvider{
    private final String csvFileName;
    private List<Question> questions;

    public GameDataProviderImpl(@Value("${questions.file.path}")String csvFileName) {
        this.csvFileName = csvFileName;
    }

    @Override
    public List<Question> getQuestions() {
        if (questions == null) {
            try {
                questions = loadQuestions(csvFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return questions;
    }

    private static List<Question> loadQuestions(String fileName) throws Exception {
        QuestionsFileReader r = new QuestionsFileReaderImpl(fileName);
        return r.loadQuestions();
    }

}
