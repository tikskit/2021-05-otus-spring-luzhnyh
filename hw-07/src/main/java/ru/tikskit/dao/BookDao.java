package ru.tikskit.dao;

import ru.tikskit.domain.Book;
import ru.tikskit.domain.BookFull;

import java.util.List;

public interface BookDao {

    Book insert(Book book);

    List<Book> getAll();

    List<BookFull> getAllFull();

    Book getById(long id);

    void update(Book book);

    void deleteById(long id);
}
