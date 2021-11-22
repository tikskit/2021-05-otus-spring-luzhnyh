package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Genre;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@DisplayName("Репозиторий для жанров должен")
@DataJpaTest
@Import({DBGenreServiceJpa.class})
class DBGenreServiceJpaTest {
    @Autowired
    private DBGenreService dbGenreService;
    @MockBean
    private GenreDao genreDao;

    @DisplayName("добавлять один и только один жанр")
    @Test
    public void saveGenreShouldInsertOneAuthor() {
        Genre newGenre = new Genre(0, "classic");
        dbGenreService.saveGenre(newGenre);
        verify(genreDao, times(1)).save(newGenre);
    }

    @DisplayName("правильно возвращать жанр по идентификатору")
    @Test
    public void getGenreShouldReturnProperEntity() {
        when(genreDao.getById(1050L)).thenReturn(new Genre(10, "sci-fi"));
        dbGenreService.getGenre(1050L);
        verify(genreDao, times(1)).getById(1050L);
    }

    @DisplayName("правильно выбирать все жанры из таблицы genres")
    @Test
    public void getAllShouldReturnAllBooks() {
        List<Genre> actual = dbGenreService.getAll();
        verify(genreDao, times(1)).findAll();
    }
}