package ru.tikskit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such genre found")
public class GenreNotFoundException extends EntityNotFoundException{
    public GenreNotFoundException() {
    }
}
