package ru.tikskit.service;

import ru.tikskit.domain.Book;
import ru.tikskit.domain.BookFull;

import java.util.List;
import java.util.Optional;

public interface DBBookService {

    Optional<Book> getBook(long id);

    Book addBook(Book book);

    void changeBook(Book book);

    void deleteBook(long id);

    List<Book> getAll();

    List<BookFull> getAllFull();
}
