package ru.tikskit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.repository.BookRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findBook(String name, String authorSurname, String authorName) {
        Optional<Author> author = authorRepository.findBySurnameAndName(authorSurname, authorName);

        if (author.isEmpty()) {
            throw new BookServiceException(String.format("Автор %s %s не найден в БД", authorSurname, authorName));
        }

        return bookRepository.findByAuthorAndNameIgnoreCase(author.get(), name);
    }
}
