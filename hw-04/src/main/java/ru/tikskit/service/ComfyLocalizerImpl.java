package ru.tikskit.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ComfyLocalizerImpl implements ComfyLocalizer {

    private final MessageSource msg;

    public ComfyLocalizerImpl(MessageSource msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage(String code) {
        return msg.getMessage(code, null, getLocale());
    }

    @Override
    public String getMessage(String code, Object... args) {
        return msg.getMessage(code, args, getLocale());
    }

    private Locale getLocale() {
        return Locale.forLanguageTag("ru-RU");
    }
}
