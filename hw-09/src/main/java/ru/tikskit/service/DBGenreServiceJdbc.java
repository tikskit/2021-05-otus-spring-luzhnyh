package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class DBGenreServiceJdbc implements DBGenreService {
    private static final Logger logger = LoggerFactory.getLogger(DBGenreServiceJdbc.class);

    private final GenreDao genreDao;

    public DBGenreServiceJdbc(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Optional<Genre> getGenre(long id) {
        try {
            Optional<Genre> genreOptional =  Optional.of(genreDao.getById(id));
            logger.info("Genre got from db: {}", genreOptional.get());
            return genreOptional;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Genre saveGenre(Genre genre) {
        try {
            Genre res = genreDao.insert(genre);
            logger.info("Genre added {}", res);
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            return genreDao.getAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return List.of();
        }
    }
}
