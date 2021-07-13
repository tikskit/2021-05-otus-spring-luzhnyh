package ru.tikskit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class OutputConsole implements Output {
    private final PrintStream out;

    public OutputConsole(@Value("#{T(java.lang.System).out}")PrintStream out) {
        this.out = out;
    }

    @Override
    public void println(String string) {
        out.println(string);
    }
}
