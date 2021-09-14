package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для жанров должен")
@DataJpaTest
@Import({DBGenreServiceJpa.class})
class DBGenreServiceJpaTest {

    @Autowired
    private DBGenreService dbGenreService;

    private Genre sciFi;
    private Genre fantasy;

    @BeforeEach
    public void setUp() {
        sciFi = dbGenreService.saveGenre(new Genre(0, "sci-fi"));
        fantasy = dbGenreService.saveGenre(new Genre(0, "fantasy"));
    }

    @DisplayName("добавлять один и только один жанр")
    @Test
    public void saveGenreShouldInsertOneAuthor() {
        List<Genre> before = dbGenreService.getAll();

        Genre classic = dbGenreService.saveGenre(new Genre(0, "classic"));

        List<Genre> now = dbGenreService.getAll();

        List<Genre> expected = new ArrayList<>(before);
        expected.add(classic);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("правильно возвращать жанр по идентификатору")
    @Test
    public void getGenreShouldReturnProperEntity() {
        Optional<Genre> testSciFi = dbGenreService.getGenre(sciFi.getId());

        assertThat(testSciFi.orElseGet(null)).usingRecursiveComparison().isEqualTo(sciFi);
    }

    @DisplayName("правильно выбирать все жанры из таблицы genres")
    @Test
    public void getAllShouldReturnAllBooks() {
        List<Genre> expected = List.of(sciFi, fantasy);

        List<Genre> actual = dbGenreService.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}