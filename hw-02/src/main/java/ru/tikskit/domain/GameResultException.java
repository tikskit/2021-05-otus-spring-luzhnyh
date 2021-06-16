package ru.tikskit.domain;

public class GameResultException extends RuntimeException{
    public GameResultException() {
    }

    public GameResultException(String message) {
        super(message);
    }
}
