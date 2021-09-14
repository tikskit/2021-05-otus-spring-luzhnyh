package ru.tikskit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such book found")
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
    }
}
