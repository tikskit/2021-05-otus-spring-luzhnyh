package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
public class DBAuthorServiceJpa implements DBAuthorService {
    private static final Logger logger = LoggerFactory.getLogger(DBAuthorServiceJpa.class);

    private final AuthorDao authorDao;

    public DBAuthorServiceJpa(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Optional<Author> getAuthor(long id) {
        try {
            Optional<Author> authorOptional = Optional.of(authorDao.getById(id));
            logger.info("Auhtor got from db: {}", authorOptional.get());
            return authorOptional;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Author saveAuthor(Author author) {
        try {
            Author res = authorDao.save(author);
            logger.info("Author added {}", res);
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            return authorDao.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return List.of();
        }
    }
}
