package ru.tikskit.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.domain.Author;
import ru.tikskit.service.cache.AuthorCache;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DBAuthorServiceJpa implements DBAuthorService {
    private static final Logger logger = LoggerFactory.getLogger(DBAuthorServiceJpa.class);

    @Autowired
    private final AuthorDao authorDao;
    @Autowired
    private final AuthorCache authorCache;

    @Override
    @HystrixCommand(commandKey = "getAuthorKey", defaultFallback = "getFromCache")
    public Optional<Author> getAuthor(long id) {
        Author author = authorDao.getById(id);
        logger.info("Auhtor got from db: {}", author);
        authorCache.put(id, author);
        return Optional.of(author);
    }

    @HystrixCommand(commandKey = "getAuthorKey", defaultFallback = "getPushkin")
    public Optional<Author>  getFromCache(long id) {
        return Optional.ofNullable(authorCache.get(id));
    }

    public Optional<Author> getPushkin() {
        return Optional.of(createPushkin());
    }

    private Author createPushkin() {
        return new Author(0L, "Пушкин", "Александр");
    }

    @Override
    public Author saveAuthor(Author author) {
        Author res = authorDao.save(author);
        logger.info("Author added {}", res);
        authorCache.put(author.getId(), author);
        return res;
    }

    @Override
    @HystrixCommand(commandKey = "getAllAuthorsKey", defaultFallback = "getAllFromCache")
    public List<Author> getAll() {
        return authorDao.findAll();
    }

    @HystrixCommand(commandKey = "getAllAuthorsKey", defaultFallback = "getAllTimeStub")
    public List<Author> getAllFromCache() {
        return authorCache.getAll();
    }

    public List<Author> getAllTimeStub() {
        return List.of(createPushkin());
    }

}
