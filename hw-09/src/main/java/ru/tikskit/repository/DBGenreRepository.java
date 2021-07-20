package ru.tikskit.repository;

import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface DBGenreRepository {

    Optional<Genre> getGenre(long id);

    Genre saveGenre(Genre genre);

    List<Genre> getAll();

}
