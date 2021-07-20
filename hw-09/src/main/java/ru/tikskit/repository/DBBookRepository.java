package ru.tikskit.repository;

import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

public interface DBBookRepository {

    Optional<Book> getBook(long id);

    Book addBook(Book book);

    void changeBook(Book book);

    void deleteBook(Book book);

    List<Book> getAll();
}
