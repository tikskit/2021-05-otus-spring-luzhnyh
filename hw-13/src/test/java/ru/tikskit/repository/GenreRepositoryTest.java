package ru.tikskit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для работы с жарнами должен")
@SpringBootTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @DisplayName("сохранять жанры в БД")
    @Test
    public void shouldSaveGenre() {
        Genre comedy = repository.save(new Genre(null, "comedy"));
        assertThat(comedy.getId()).isNotEmpty();
    }

    @DisplayName("загружать жанры по идентификатору")
    @Test
    public void shouldLoadGenreById() {
        Genre comedy = repository.save(new Genre(null, "comedy"));
        Optional<Genre> loaded = repository.findById(comedy.getId());

        assertThat(loaded).isNotEmpty().get().usingRecursiveComparison().isEqualTo(comedy);
    }

    @DisplayName("загружать все жанры из БД")
    @Test
    public void shouldLoadAllGenres() {
        repository.saveAll(List.of(new Genre(null, "sci-fi"), new Genre(null, "fantasy")));

        List<Genre> all = repository.findAll();
        assertThat(all).
                extracting("name").
                containsExactlyInAnyOrderElementsOf(List.of("sci-fi", "fantasy"));
    }

    @DisplayName("выбрасывать исключение, когда нарушается уникальность имени жарна")
    @Test
    public void shouldThrowExceptionWhenUniqueNameViolated() {
        repository.save(new Genre(null, "comedy"));
        assertThatThrownBy(() -> repository.save(new Genre(null, "comedy"))).
                isInstanceOf(Exception.class);
    }

    @DisplayName("обновлять в БД жанр")
    @Test
    public void shouldUpdateGenre() {
        Genre fantasy = repository.save(new Genre(null, "fantasy"));
        fantasy.setName("detective");
        Genre detective = repository.save(fantasy);

        Optional<Genre> actual = repository.findById(detective.getId());
        assertThat(actual).isPresent().get().extracting("name").isEqualTo(detective.getName());
    }

    @DisplayName("находить жанр по наименованию")
    @Test
    public void shouldFindGenreByName() {
        Genre fantasy = repository.save(new Genre(null, "fantasy"));

        Optional<Genre> actual = repository.findByName("fantasy");

        assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(fantasy);
    }
}