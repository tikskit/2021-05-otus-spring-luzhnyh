package ru.tikskit.service.cache;

import ru.tikskit.domain.Author;

import java.util.List;

public interface AuthorCache {
    Author get(long id);
    Author put(long id, Author author);
    List<Author> getAll();
}
