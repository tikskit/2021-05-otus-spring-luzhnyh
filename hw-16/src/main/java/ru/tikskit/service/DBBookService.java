package ru.tikskit.service;

import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

public interface DBBookService {

    Optional<Book> getBook(long id);

    Book addBook(Book book);

    Book changeBook(Book book);

    void deleteBook(Book book);

    List<Book> getAll();
}
