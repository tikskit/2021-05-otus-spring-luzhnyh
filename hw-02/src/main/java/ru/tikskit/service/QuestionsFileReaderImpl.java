package ru.tikskit.service;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

class QuestionsFileReaderImpl implements QuestionsFileReader {
    private final String csvFileName;

    public QuestionsFileReaderImpl(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    @Override
    public List<String[]> readCsv() throws Exception {
        try (Reader reader = new BufferedReader(
                new InputStreamReader(
                        GameDataProvider.class.getResourceAsStream(csvFileName)))) {
            CSVReader csvReader = new CSVReader(reader);
            return csvReader.readAll();
        }
    }
}
