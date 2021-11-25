package ru.tikskit.service;

import org.springframework.stereotype.Service;
import ru.tikskit.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
public interface DBAuthorService {

    Optional<Author> getAuthor(long id);

    Author saveAuthor(Author author);

    List<Author> getAll();
}
