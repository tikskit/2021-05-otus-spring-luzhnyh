package ru.tikskit.dto;

import org.springframework.stereotype.Component;
import ru.tikskit.domain.Author;

@Component
public class AuthorConverter {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getSurname(), author.getName());
    }
}
