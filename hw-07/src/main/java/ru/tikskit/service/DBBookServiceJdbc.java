package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.BookDao;
import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

@Service
public class DBBookServiceJdbc implements DBBookService {

    private static final Logger logger = LoggerFactory.getLogger(DBBookServiceJdbc.class);

    private final BookDao bookDao;

    public DBBookServiceJdbc(BookDao bookDao) {
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
    public void addBook(Book book) {
        try {
            bookDao.insert(book);
            logger.info("Book added {}", book);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void changeBook(Book book) {
        try {
            bookDao.update(book);
            logger.info("Book updated {}", book);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteBook(long id) {
        try {
            bookDao.deleteById(id);
            logger.info("Book deleted {}", id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return bookDao.getAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return List.of();
        }
    }
}
