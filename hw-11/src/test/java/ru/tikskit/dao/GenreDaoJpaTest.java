package ru.tikskit.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с жанрами должно")
@DataJpaTest
class GenreDaoJpaTest {

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private TestEntityManager em;

    @DisplayName("добавлять методом insert один жанр")
    @Test
    public void insertShouldCreateOneGenre() {
        Genre expectedGenre = genreDao.save(new Genre(0, "sci-fi"));

        assertThat(expectedGenre.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

        Genre actualGenre = em.find(Genre.class, expectedGenre.getId());
        assertThat(expectedGenre).usingRecursiveComparison()
                .isEqualTo(actualGenre);
    }

    @DisplayName("возвращать все жанры из таблицы genres")
    @Test
    public void getAllShouldReturnAllGenres(){
        List<String> genres = List.of("sci-fi", "fantasy", "autobiography");

        for (String genre : genres) {
            em.persist(new Genre(0, genre));
        }

        List<Genre> actualGenres = genreDao.findAll();
        assertThat(actualGenres).
                extracting("name", String.class).
                as("check that exist only genres we've just added").
                containsExactlyInAnyOrderElementsOf(genres);
    }
}