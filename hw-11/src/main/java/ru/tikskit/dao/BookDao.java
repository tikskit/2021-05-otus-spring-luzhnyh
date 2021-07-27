package ru.tikskit.dao;

import ru.tikskit.domain.Book;

import java.util.List;

public interface BookDao {

    Book insert(Book book);

    List<Book> getAll();

    Book getById(long id);

    Book update(Book book);

    void delete(Book book);
}
