package ru.tikskit.service;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Scanner;


@Service
class QuestionIOConsole implements QuestionIO {

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
        Scanner s = new Scanner(System.in);

        Integer optionIndex;
        do {
            System.out.print("Type your answer: ");
            String value = s.nextLine();
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
