package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.AuthorDaoJdbc;
import ru.tikskit.domain.Author;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Сервис для работы с авторами должен")
@JdbcTest
@Import({DBAuthorServiceJdbc.class, AuthorDaoJdbc.class})
class DBAuthorServiceJdbcTest {
    @Autowired
    private DBAuthorService dbAuthorService;

    @DisplayName("добавлять одного и только одного автора")
    @Test
    public void saveAuthorShouldInsertOneAuthor() {
        Author lukyanenko = new Author(0, "Лукьяненко", "Сергей");
        dbAuthorService.saveAuthor(lukyanenko);
        Author vasilyev = new Author(0, "Васильев", "Сергей");
        dbAuthorService.saveAuthor(vasilyev);

        List<Author> before = dbAuthorService.getAll();

        Author gaiman = new Author(0, "Гейман", "Нил");
        dbAuthorService.saveAuthor(gaiman);

        List<Author> now = dbAuthorService.getAll();

        List<Author> expected = new ArrayList<>(before);
        expected.add(gaiman);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }

}