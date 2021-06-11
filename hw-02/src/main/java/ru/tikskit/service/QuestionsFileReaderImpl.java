package ru.tikskit.service;

import com.opencsv.CSVReader;
import ru.tikskit.domain.Option;
import ru.tikskit.domain.Question;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class QuestionsFileReaderImpl implements QuestionsFileReader {
    private final String csvFileName;

    public QuestionsFileReaderImpl(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    @Override
    public List<Question> loadQuestions() throws Exception {
        List<String[]> csvContent = readCsv();
        List<Question> questions = new ArrayList<>();
        for (String[] line : csvContent) {
            List<Option> options = Arrays.stream(line, 1, line.length).
                    map(Option::new).
                    collect(Collectors.toList());
            questions.add(new Question(line[0], options, options.get(0)));
        }
        return questions;
    }

    private List<String[]> readCsv() throws Exception {
        try (Reader reader = new BufferedReader(
                new InputStreamReader(
                        GameDataProvider.class.getResourceAsStream(csvFileName)))) {
            CSVReader csvReader = new CSVReader(reader);
            return csvReader.readAll();
        }
    }
}
