package ru.tikskit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Such not author found")
public class AuthorNotFoundException extends EntityNotFoundException{
    public AuthorNotFoundException() {
    }

    public AuthorNotFoundException(Throwable cause) {
        super(cause);
    }
}
