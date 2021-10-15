package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.repository.GenreRepository;
import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class DBGenreServiceJpa implements DBGenreService {
    private static final Logger logger = LoggerFactory.getLogger(DBGenreServiceJpa.class);

    private final GenreRepository genreRepository;

    public DBGenreServiceJpa(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<Genre> getGenre(long id) {
        Optional<Genre> genreOptional =  Optional.of(genreRepository.getById(id));
        logger.info("Genre got from db: {}", genreOptional.get());
        return genreOptional;
    }

    @Override
    public Genre saveGenre(Genre genre) {
        Genre res = genreRepository.save(genre);
        logger.info("Genre added {}", res);
        return res;
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
