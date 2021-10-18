package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
public class DBAuthorServiceJpa implements DBAuthorService {
    private static final Logger logger = LoggerFactory.getLogger(DBAuthorServiceJpa.class);

    private final AuthorRepository authorRepository;

    public DBAuthorServiceJpa(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<Author> getAuthor(long id) {
        Optional<Author> authorOptional = Optional.of(authorRepository.getById(id));
        logger.info("Auhtor got from db: {}", authorOptional.get());
        return authorOptional;
    }

    @Override
    public Author saveAuthor(Author author) {
        Author res = authorRepository.save(author);
        logger.info("Author added {}", res);
        return res;
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }
}
