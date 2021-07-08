package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.GenreDaoJdbc;
import ru.tikskit.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Сервис для жанров должен")
@JdbcTest
@Import({DBGenreServiceJdbc.class, GenreDaoJdbc.class})
class DBGenreServiceJdbcTest {
    @Autowired
    private DBGenreService dbGenreService;

    @DisplayName("добавлять один и только один жанр")
    @Test
    public void saveGenreShouldInsertOneAuthor() {
        Genre sciFi = new Genre(0, "sci-fi");
        dbGenreService.saveGenre(sciFi);
        Genre fantasy = new Genre(0, "fantasy");
        dbGenreService.saveGenre(fantasy);

        List<Genre> before = dbGenreService.getAll();

        Genre classic = new Genre(0, "classic");
        dbGenreService.saveGenre(classic);

        List<Genre> now = dbGenreService.getAll();

        List<Genre> expected = new ArrayList<>(before);
        expected.add(classic);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);

    }
}