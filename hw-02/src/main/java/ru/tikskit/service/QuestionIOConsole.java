package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.Question;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;


@Service
class QuestionIOConsole implements QuestionIO {

    private final InputStream in;
    private final PrintStream out;


    public QuestionIOConsole(@Value("#{T(java.lang.System).in}") InputStream in,
                             @Value("#{T(java.lang.System).out}") PrintStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public int askQuestion(Question question) {
        Objects.requireNonNull(question);

        printQuestion(question);
        return getUserAnswer(question);
    }

    private void printQuestion(Question question) {
        out.println(question.getText());
        for (int i = 0; i < question.getOptions().size(); i++) {
            out.println(String.format("%d. %s", i + 1, question.getOptions().get(i).getText()));
        }
    }


    private int getUserAnswer(Question question) {
        Scanner s = new Scanner(in);

        Integer answerIndex;
        do {
            out.print("Type your answer: ");
            String value = s.nextLine();
            answerIndex = getAnswerIndex(value, question);

            if (answerIndex == null) {
                out.println(String.format("Can't accept the answer: \'%s\'! Try again", value));
            }

        } while (answerIndex == null);
        return answerIndex;
    }

    private static Integer getAnswerIndex(String value, Question question) {
        int index;

        try {
            index = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }

        index--;

        if (index < 0 || index >= question.getOptions().size()) {
            return null;
        }

        return index;
    }
}
