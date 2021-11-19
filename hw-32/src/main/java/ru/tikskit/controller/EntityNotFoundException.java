package ru.tikskit.controller;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
