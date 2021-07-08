package ru.tikskit.dao;

import ru.tikskit.domain.Genre;

import java.util.List;

public interface GenreDao {

    Genre getById(long id);

    void insert(Genre genre);

    List<Genre> getAll();
}
