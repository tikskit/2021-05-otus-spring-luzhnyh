package ru.tikskit.service;

import ru.tikskit.domain.Author;

import java.util.List;
import java.util.Optional;

public interface DBAuthorService {

    Optional<Author> getAuthor(long id);

    Author saveAuthor(Author author);

    List<Author> getAll();
}
