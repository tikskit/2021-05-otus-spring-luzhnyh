package ru.tikskit.service;

import ru.tikskit.domain.Book;

import java.util.Optional;

public interface BookService {
    Optional<Book> findBook(String name, String authorSurname, String authorName);
}
