package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.AuthorDaoJdbc;
import ru.tikskit.domain.Author;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Сервис для работы с авторами должен")
@JdbcTest
@Import({DBAuthorServiceJdbc.class, AuthorDaoJdbc.class})
class DBAuthorServiceJdbcTest {
    @Autowired
    private DBAuthorService dbAuthorService;

    private Author lukyanenko;
    private Author vasilyev;

    @BeforeEach
    public void setUp() {
        lukyanenko = dbAuthorService.saveAuthor(new Author(0, "Лукьяненко", "Сергей"));
        vasilyev = dbAuthorService.saveAuthor(new Author(0, "Васильев", "Сергей"));
    }

    @DisplayName("добавлять одного и только одного автора")
    @Test
    public void saveAuthorShouldInsertOneAuthor() {
        List<Author> before = dbAuthorService.getAll();

        Author gaiman = dbAuthorService.saveAuthor(new Author(0, "Гейман", "Нил"));

        List<Author> now = dbAuthorService.getAll();

        List<Author> expected = new ArrayList<>(before);
        expected.add(gaiman);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("правильно возвращать автора по идентификатору")
    @Test
    public void getAuthorShouldReturnProperEntity() {
        Optional<Author> testLukyaninko = dbAuthorService.getAuthor(lukyanenko.getId());

        assertThat(testLukyaninko.orElseGet(null)).usingRecursiveComparison().isEqualTo(lukyanenko);
    }

    @DisplayName("правильно выбирать всех авторов из таблицы authors")
    @Test
    public void getAllShouldReturnAllBooks() {
        List<Author> expected = List.of(vasilyev, lukyanenko);

        List<Author> actual = dbAuthorService.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}