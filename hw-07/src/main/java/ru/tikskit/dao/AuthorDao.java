package ru.tikskit.dao;

import ru.tikskit.domain.Author;

import java.util.List;

public interface AuthorDao {
    void insert(Author author);
    List<Author> getAll();
    Author getById(long id);
}
