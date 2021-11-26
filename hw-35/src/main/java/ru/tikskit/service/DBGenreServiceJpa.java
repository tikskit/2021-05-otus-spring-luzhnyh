package ru.tikskit.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Genre;
import ru.tikskit.service.cache.Cache;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DBGenreServiceJpa implements DBGenreService {
    private static final Logger logger = LoggerFactory.getLogger(DBGenreServiceJpa.class);

    private final GenreDao genreDao;
    private final Cache<Genre> genreCache;

    @Override
    @HystrixCommand(commandKey = "getGenreKey", fallbackMethod = "getGenreFromCache")
    public Optional<Genre> getGenre(long id) {
        Optional<Genre> genreOptional =  genreDao.findById(id);
        logger.info("Genre got from db: {}", genreOptional.get());
        genreOptional.ifPresent(g -> genreCache.put(id, g));
        return genreOptional;
    }

    @HystrixCommand(commandKey = "getGenreKey", fallbackMethod = "getGenreStub")
    public Optional<Genre> getGenreFromCache(long id) {
        return genreCache.get(id);
    }

    public Optional<Genre> getGenreStub(long id) {
        return Optional.of(createGenreStub());
    }

    @Override
    @HystrixCommand(commandKey = "saveGenreKey", fallbackMethod = "saveGenreToCache")
    public Genre saveGenre(Genre genre) {
        Genre res = genreDao.save(genre);
        logger.info("Genre added {}", res);
        genreCache.put(genre.getId(), genre);
        return res;
    }

    public Genre saveGenreToCache(Genre genre) {
        if (genre.getId() > 0) {
            genreCache.put(genre.getId(), genre);
        }
        return genre;
    }

    @Override
    @HystrixCommand(commandKey = "getAllGenreKey", fallbackMethod = "getAllFromCache")
    public List<Genre> getAll() {
        return genreDao.findAll();
    }

    @HystrixCommand(commandKey = "getAllGenreKey", fallbackMethod = "getAllGenreStub")
    public List<Genre> getAllFromCache() {
        return genreCache.getAll();
    }

    public List<Genre> getAllGenreStub() {
        return List.of(createGenreStub());
    }

    private Genre createGenreStub() {
        return new Genre(0L, "Любой жанр");
    }
}
