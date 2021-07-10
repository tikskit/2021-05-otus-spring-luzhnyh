package ru.tikskit.dao;

import ru.tikskit.domain.Author;

import java.util.List;

public interface AuthorDao {

    Author insert(Author author);

    List<Author> getAll();

    Author getById(long id);
}
