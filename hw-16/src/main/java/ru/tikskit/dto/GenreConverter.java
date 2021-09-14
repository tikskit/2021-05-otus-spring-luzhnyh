package ru.tikskit.dto;

import org.springframework.stereotype.Component;
import ru.tikskit.domain.Genre;

@Component
public class GenreConverter {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
