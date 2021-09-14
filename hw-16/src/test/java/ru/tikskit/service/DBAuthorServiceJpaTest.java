package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с авторами должен")
@DataJpaTest
@Import({DBAuthorServiceJpa.class})
class DBAuthorServiceJpaTest {

    @Autowired
    private DBAuthorService authorService;

    private Author lukyanenko;
    private Author vasilyev;

    @BeforeEach
    public void setUp() {
        lukyanenko = authorService.saveAuthor(new Author(0, "Лукьяненко", "Сергей"));
        vasilyev = authorService.saveAuthor(new Author(0, "Васильев", "Сергей"));
    }

    @DisplayName("добавлять одного и только одного автора")
    @Test
    public void saveAuthorShouldInsertOneAuthor() {
        List<Author> before = authorService.getAll();

        Author gaiman = authorService.saveAuthor(new Author(0, "Гейман", "Нил"));

        List<Author> now = authorService.getAll();

        List<Author> expected = new ArrayList<>(before);
        expected.add(gaiman);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("правильно возвращать автора по идентификатору")
    @Test
    public void getAuthorShouldReturnProperEntity() {
        Optional<Author> testLukyaninko = authorService.getAuthor(lukyanenko.getId());

        assertThat(testLukyaninko.orElseGet(null)).usingRecursiveComparison().isEqualTo(lukyanenko);
    }

    @DisplayName("правильно выбирать всех авторов из таблицы authors")
    @Test
    public void getAllShouldReturnAllBooks() {
        List<Author> expected = List.of(vasilyev, lukyanenko);

        List<Author> actual = authorService.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}