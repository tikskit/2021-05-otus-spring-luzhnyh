package ru.tikskit.rest.dto;

import org.springframework.stereotype.Component;
import ru.tikskit.domain.Author;

@Component
public class AuthorConverter {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName(), author.getSurname());
    }

    public Author toAuthor(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getSurname(), authorDto.getName());
    }

}
