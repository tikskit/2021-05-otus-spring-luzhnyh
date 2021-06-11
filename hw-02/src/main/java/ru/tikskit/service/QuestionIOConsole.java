package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;


@Service
class QuestionIOConsole implements QuestionIO {

    private final BufferedReader reader;

    public QuestionIOConsole(
            @Value("#{new java.io.BufferedReader(new java.io.InputStreamReader(T(java.lang.System).in))}")
                    BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public int askQuestion(String questionText, String[] options) {
        Objects.requireNonNull(questionText);
        Objects.requireNonNull(options);

        printQuestion(questionText, options);
        return getUserAnswer(options.length);
    }

    private void printQuestion(String questionText, String[] options) {
        System.out.println(questionText);

        for (int i = 0; i < options.length; i++) {
            System.out.println(String.format("%d. %s", i + 1, options[i]));
        }
    }

    private int getUserAnswer(int optionsCount) {
        Integer optionIndex;
        do {
            System.out.print("Type your answer number: ");
            String value = null;
            try {
                value = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            optionIndex = typedValue2OptionIndex(value, optionsCount);

            if (optionIndex == null) {
                System.out.println(String.format("Can't accept the answer: '%s'! Try again", value));
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
