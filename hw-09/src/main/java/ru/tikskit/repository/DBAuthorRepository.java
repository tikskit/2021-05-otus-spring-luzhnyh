package ru.tikskit.repository;

import ru.tikskit.domain.Author;

import java.util.List;
import java.util.Optional;

public interface DBAuthorRepository {

    Optional<Author> getAuthor(long id);

    Author saveAuthor(Author author);

    List<Author> getAll();
}
