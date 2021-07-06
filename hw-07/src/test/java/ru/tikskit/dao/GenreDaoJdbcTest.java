package ru.tikskit.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("выбрасывать исключение при попытке добавить несколько одинаковых жанров")
    @Test
    public void genreCanBeAddedOnlyOnce() {
        Genre genre1 = new Genre(0, "fantasy");
        genreDao.insert(genre1);
        Genre genre2 = new Genre(0, "fantasy");
        assertThatThrownBy(() -> genreDao.insert(genre2)).
                as("check unique genre constraint").
                isInstanceOf(RuntimeException.class);
    }

    @DisplayName("добавлять методом insert один жанр")
    @Test
    public void insertShouldCreateOneGenre() {
        Genre expectedGenre = new Genre(0, "sci-fi");
        genreDao.insert(expectedGenre);

        assertThat(expectedGenre.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(expectedGenre).usingRecursiveComparison()
                .isEqualTo(actualGenre);
    }

    @DisplayName("возвращать все жанры из таблицы genres")
    @Test
    public void getAllShouldReturnAllGenres(){
        List<String> genres = List.of("sci-fi", "fantasy", "autobiography");

        for (String genre : genres) {
            genreDao.insert(new Genre(0, genre));
        }

        List<Genre> actualGenres = genreDao.getAll();
        assertThat(actualGenres).
                extracting("name", String.class).
                as("check that exist only genres we've just added").
                containsExactlyInAnyOrderElementsOf(genres);
    }

}