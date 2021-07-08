package ru.tikskit.service;

import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface DBGenreService {

    Optional<Genre> getGenre(long id);

    void saveGenre(Genre genre);

    List<Genre> getAll();

}
