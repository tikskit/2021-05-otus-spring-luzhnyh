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
        Optional<Author> authorOptional = Optional.of(authorDao.getById(id));
        logger.info("Auhtor got from db: {}", authorOptional.get());
        return authorOptional;
    }

    @Override
    public Author saveAuthor(Author author) {
        Author res = authorDao.save(author);
        logger.info("Author added {}", res);
        return res;
    }

    @Override
    public List<Author> getAll() {
        return authorDao.findAll();
    }
}
