package ru.tikskit.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

@Service
public class QuestionAnswerRequestConsole implements QuestionAnswerRequest {
    public static final String REQUEST_MESSAGE = "Type your answer number: ";
    public static final String WRONG_INPUT_TEMPLATE = "Can't accept the answer: '%s'! Try again";

    private final BufferedReader reader;
    private final PrintStream out;

    public QuestionAnswerRequestConsole(
            @Value("#{new java.io.BufferedReader(new java.io.InputStreamReader(T(java.lang.System).in))}")
                    BufferedReader reader,
            @Value("#{T(java.lang.System).out}") PrintStream out) {
        this.reader = reader;
        this.out = out;
    }

    @Override
    public int requestAnswerNo(int optionsCount) {
        Integer optionIndex;
        do {
            out.print(REQUEST_MESSAGE);
            String value = null;
            try {
                value = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            optionIndex = typedValue2OptionIndex(value, optionsCount);

            if (optionIndex == null) {
                out.println(String.format(WRONG_INPUT_TEMPLATE, value));
            }

        } while (optionIndex == null);
        return optionIndex;
    }
    private static Integer typedValue2OptionIndex(String value, int optionsCount) {
        int index;

        try {
            index = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }

        index--;

        if (index < 0 || index >= optionsCount) {
            return null;
        }

        return index;
    }
}
