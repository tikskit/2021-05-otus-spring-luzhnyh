package ru.tikskit.repository;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tikskit.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для авторов должен")
@SpringBootTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @DisplayName("сохранять авторов в БД")
    @Test
    public void shouldSaveAuthor() {
        Author shildt = repository.save(new Author(null, "Шалдт", "Герберт"));
        assertThat(shildt.getId()).isNotEmpty();
    }

    @DisplayName("загружать авторов по идентификатору")
    @Test
    public void shouldLoadAuthorById() {
        Author shildt = repository.save(new Author(null, "Шалдт", "Герберт"));
        Optional<Author> loaded = repository.findById(shildt.getId());

        assertThat(loaded).isNotEmpty().get().usingRecursiveComparison().isEqualTo(shildt);
    }

    @DisplayName("загружать всех авторов из БД")
    @Test
    public void shouldLoadAllGenres() {
        repository.saveAll(List.of(
                new Author(null, "Васильев", "Владимир"),
                new Author(null, "Лукьяненко", "Сергей")));

        List<Author> all = repository.findAll();
        assertThat(all).
                extracting("surname", "name").
                containsExactlyInAnyOrderElementsOf(
                        List.of(
                                new Tuple("Васильев", "Владимир"),
                                new Tuple("Лукьяненко", "Сергей")
                        )
                );
    }

    @DisplayName("выбрасывать исключение, когда нарушается уникальность имен авторов")
    @Test
    public void shouldThrowExceptionWhenUniqueAuthorNameViolated() {
        repository.save(new Author(null, "Васильев", "Владимир"));
        assertThatThrownBy(() -> repository.save(new Author(null, "Васильев", "Владимир"))).
                isInstanceOf(Exception.class);
    }

    @DisplayName("Обновлять в БД автора")
    @Test
    public void shouldUpdateGenre() {
        Author lukyanenko = repository.save(new Author(null, "Лукьяненко", "Сергей"));
        lukyanenko.setSurname("Васильев");
        lukyanenko.setName("Владимир");
        Author vasilyev = repository.save(lukyanenko);

        Optional<Author> actual = repository.findById(vasilyev.getId());
        assertThat(actual).isPresent().get().usingRecursiveComparison().ignoringFields("id").isEqualTo(vasilyev);
    }

    @DisplayName("Загружать автора по фамилии и имени")
    @Test
    public void shouldFindAuthorBySurnameAndName() {
        Author lukyanenko = repository.save(new Author(null, "Лукьяненко", "Сергей"));

        Optional<Author> actual = repository.findBySurnameAndName("Лукьяненко", "Сергей");

        assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(lukyanenko);
    }

}