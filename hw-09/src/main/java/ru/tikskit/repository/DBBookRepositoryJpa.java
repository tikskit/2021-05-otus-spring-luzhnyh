package ru.tikskit.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.tikskit.dao.BookDao;
import ru.tikskit.domain.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class DBBookRepositoryJpa implements DBBookRepository {
    private static final Logger logger = LoggerFactory.getLogger(DBBookRepositoryJpa.class);

    private final BookDao bookDao;

    public DBBookRepositoryJpa(BookDao bookDao) {
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
            Book res = bookDao.insert(book);
            logger.info("Book added {}", res);
            return res;
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
    public void deleteBook(Book book) {
        try {
            bookDao.deleteById(book);
            logger.info("Book deleted {}", book);
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
