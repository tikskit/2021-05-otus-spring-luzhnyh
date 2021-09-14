package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.BookDao;
import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

@Service
public class DBBookServiceJpa implements DBBookService {
    private static final Logger logger = LoggerFactory.getLogger(DBBookServiceJpa.class);

    private final BookDao bookDao;

    public DBBookServiceJpa(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Optional<Book> getBook(long id) {
        try {
            Optional<Book> bookOptional = Optional.of(bookDao.getById(id));
            logger.info("Book got from db: {}", bookOptional.get());
            return bookOptional;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Book addBook(Book book) {
        try {
            Book res = bookDao.save(book);
            logger.info("Book added {}", res);
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Book changeBook(Book book) {
        try {
            Book updated = bookDao.save(book);
            logger.info("Book updated {}", updated);
            return updated;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteBook(Book book) {
        try {
            bookDao.delete(book);
            logger.info("Book deleted {}", book);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return bookDao.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return List.of();
        }
    }
}
