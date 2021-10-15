package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

@Service
public class DBBookServiceJpa implements DBBookService {
    private static final Logger logger = LoggerFactory.getLogger(DBBookServiceJpa.class);

    private final BookRepository bookRepository;

    public DBBookServiceJpa(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> getBook(long id) {
        Optional<Book> bookOptional = Optional.of(bookRepository.getById(id));
        logger.info("Book got from db: {}", bookOptional.get());
        return bookOptional;
    }

    @Override
    public Book addBook(Book book) {
        Book res = bookRepository.save(book);
        logger.info("Book added {}", res);
        return res;
    }

    @Override
    public Book changeBook(Book book) {
        Book updated = bookRepository.save(book);
        logger.info("Book updated {}", updated);
        return updated;
    }

    @Override
    public void deleteBook(Book book) {
        bookRepository.delete(book);
        logger.info("Book deleted {}", book);
    }

    @Override
    public void deleteBookById(long id) {
        bookRepository.deleteById(id);
        logger.info("Book deleted {}", id);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}
