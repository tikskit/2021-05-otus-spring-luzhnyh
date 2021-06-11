package ru.tikskit.service;

import java.util.List;

interface QuestionsFileReader {
    List<String[]> readCsv() throws Exception;
}
