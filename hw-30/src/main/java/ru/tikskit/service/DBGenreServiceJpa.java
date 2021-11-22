package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class DBGenreServiceJpa implements DBGenreService {
    private static final Logger logger = LoggerFactory.getLogger(DBGenreServiceJpa.class);

    private final GenreDao genreDao;

    public DBGenreServiceJpa(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Optional<Genre> getGenre(long id) {
        Optional<Genre> genreOptional =  Optional.of(genreDao.getById(id));
        logger.info("Genre got from db: {}", genreOptional.get());
        return genreOptional;
    }

    @Override
    public Genre saveGenre(Genre genre) {
        Genre res = genreDao.save(genre);
        logger.info("Genre added {}", res);
        return res;
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.findAll();
    }
}
