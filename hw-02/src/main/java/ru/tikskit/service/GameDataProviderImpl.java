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
    private final List<Question> questions;

    public GameDataProviderImpl(@Value("${questions.file.path}")String csvFileName) throws Exception {
        List<String[]> csvContent = readGameDataFromFile(csvFileName);
        questions = new ArrayList<>();
        for (String[] line : csvContent) {
            List<Option> options = Arrays.stream(line, 1, line.length).
                    map(Option::new).
                    collect(Collectors.toList());
            questions.add(new Question(line[0], options, options.get(0)));
        }
    }


    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    private List<String[]> readGameDataFromFile(String csvFileName) throws Exception {
        QuestionsFileReader r = new QuestionsFileReaderImpl(csvFileName);
        return r.readCsv();
    }

}
