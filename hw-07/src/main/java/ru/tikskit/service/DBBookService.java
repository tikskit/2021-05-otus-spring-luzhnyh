package ru.tikskit.service;

import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

public interface DBBookService {

    Optional<Book> getBook(long id);

    void saveBook(Book book);

    void deleteBook(long id);

    List<Book> getAll();
}
