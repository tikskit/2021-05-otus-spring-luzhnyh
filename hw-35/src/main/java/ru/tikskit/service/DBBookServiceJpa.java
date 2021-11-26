package ru.tikskit.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.BookDao;
import ru.tikskit.domain.Book;
import ru.tikskit.service.cache.Cache;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DBBookServiceJpa implements DBBookService {
    private static final Logger logger = LoggerFactory.getLogger(DBBookServiceJpa.class);

    private final BookDao bookDao;
    private final Cache<Book> bookCache;

    @Override
    @HystrixCommand(commandKey = "getBookKey", fallbackMethod = "getBookFromCache")
    public Optional<Book> getBook(long id) {
        Optional<Book> bookOptional = bookDao.findById(id);
        logger.info("Book got from db: {}", bookOptional.get());
        return bookOptional;
    }

    public Optional<Book> getBookFromCache(long id) {
        return Optional.ofNullable(bookCache.get(id));
    }

    @Override
    public Book addBook(Book book) {
        Book res = bookDao.save(book);
        logger.info("Book added {}", res);
        bookCache.put(book.getId(), book);
        return res;
    }

    @Override
    public Book changeBook(Book book) {
        Book updated = bookDao.save(book);
        logger.info("Book updated {}", updated);
        bookCache.put(book.getId(), book);
        return updated;
    }

    @Override
    public void deleteBook(Book book) {
        bookDao.delete(book);
        logger.info("Book deleted {}", book);
    }

    @Override
    public void deleteBookById(long id) {
        bookDao.deleteById(id);
        logger.info("Book deleted {}", id);
    }

    @Override
    @HystrixCommand(commandKey = "getAllBooksKey", fallbackMethod = "getAllFromCache")
    public List<Book> getAll() {
        return bookDao.findAll();
    }

    public List<Book> getAllFromCache() {
        return bookCache.getAll();
    }
}
