package ru.tikskit.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.tikskit.config.LocalizationConfig;

import java.util.Locale;

@Service
public class ComfyLocalizerImpl implements ComfyLocalizer {

    private final MessageSource msg;
    private final LocalizationConfig config;

    public ComfyLocalizerImpl(MessageSource msg, LocalizationConfig config) {
        this.msg = msg;
        this.config = config;
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
        return Locale.forLanguageTag(config.getLocaleTag());
    }
}
