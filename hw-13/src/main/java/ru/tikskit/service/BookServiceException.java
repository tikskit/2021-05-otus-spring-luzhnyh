package ru.tikskit.service;

public class BookServiceException extends RuntimeException {
    public BookServiceException() {
    }

    public BookServiceException(String message) {
        super(message);
    }
}
