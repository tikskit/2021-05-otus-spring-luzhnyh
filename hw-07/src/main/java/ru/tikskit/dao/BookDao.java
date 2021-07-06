package ru.tikskit.dao;

import ru.tikskit.domain.Book;

import java.util.List;

public interface BookDao {

    void insert(Book book);

    List<Book> getAll();

    Book getById(long id);

    void update(Book book);

    void deleteById(long id);
}
