package ru.tikskit.service;

import com.opencsv.CSVReader;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameDataProviderImpl implements GameDataProvider{
    private final List<Question> questions;

    public GameDataProviderImpl(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> csvContent = csvReader.readAll();

        questions = new ArrayList<>();
        for (String[] line : csvContent) {
            List<Option> options = Arrays.stream(line, 1, line.length).
                    map(Option::new).
                    collect(Collectors.toList());
            questions.add(new Question(line[0], options));
        }
    }


    @Override
    public List<Question> getQuestions() {
        return questions;
    }


    public static GameDataProvider createInstance() throws Exception {
        try (Reader reader = new BufferedReader(
                new InputStreamReader(
                        GameDataProvider.class.getResourceAsStream("/questions.csv")))) {

            return new GameDataProviderImpl(reader);
        }
    }
}
