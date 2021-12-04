package ru.tikskit.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.domain.Author;
import ru.tikskit.service.cache.Cache;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DBAuthorServiceJpa implements DBAuthorService {
    private static final Logger logger = LoggerFactory.getLogger(DBAuthorServiceJpa.class);

    @Autowired
    private final AuthorDao authorDao;
    @Autowired
    private final Cache<Author> authorCache;

    @Override
    @HystrixCommand(commandKey = "getAuthorKey", fallbackMethod = "getFromCache")
    public Optional<Author> getAuthor(long id) {
        Optional<Author> author = authorDao.findById(id);
        logger.info("Auhtor got from db: {}", author.orElse(null));
        author.ifPresent(value -> authorCache.put(id, value));
        return author;
    }

    @HystrixCommand(commandKey = "getAuthorKey", fallbackMethod = "getPushkin")
    public Optional<Author> getFromCache(long id) {
        return authorCache.get(id);
    }

    public Optional<Author> getPushkin(long id) {
        return Optional.of(createPushkin());
    }

    @Override
    @HystrixCommand(commandKey = "saveAuthorKey", fallbackMethod = "saveAuthorToCache")
    public Author saveAuthor(Author author) {
        Author res = authorDao.save(author);
        logger.info("Author added {}", res);
        authorCache.put(author.getId(), author);
        return res;
    }

    public Author saveAuthorToCache(Author author) {
        if (author.getId() > 0) {
            authorCache.put(author.getId(), author);
        }
        return author;
    }

    @Override
    @HystrixCommand(commandKey = "getAllAuthorsKey", fallbackMethod = "getAllFromCache")
    public List<Author> getAll() {
        return authorDao.findAll();
    }

    @HystrixCommand(commandKey = "getAllAuthorsKey", fallbackMethod = "getAllTimeStub")
    public List<Author> getAllFromCache() {
        return authorCache.getAll();
    }

    public List<Author> getAllTimeStub() {
        return List.of(createPushkin());
    }

    private Author createPushkin() {
        return new Author(0L, "Пушкин", "Александр");
    }
}
