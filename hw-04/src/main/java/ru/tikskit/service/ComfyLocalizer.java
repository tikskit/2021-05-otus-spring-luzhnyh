package ru.tikskit.service;

public interface ComfyLocalizer {
    String getMessage(String code);
    String getMessage(String code, Object... args);
}
