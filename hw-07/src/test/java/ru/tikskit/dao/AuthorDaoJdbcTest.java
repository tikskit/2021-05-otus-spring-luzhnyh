package ru.tikskit.dao;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Author;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("устанавливать идентификатор объекта при его сохранении в БД")
    @Test
    public void checkSavingAuthorSetsId() {
        Author expected = new Author(0, "Васильев", "Владимир");
        authorDao.insert(expected);

        assertThat(expected.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

    }

    @DisplayName("добавлять методом insert одного автора")
    @Test
    public void insertShouldCreateOneAuthor() {
        Author expectedAuthor = new Author(0, "Васильев", "Владимир");
        authorDao.insert(expectedAuthor);

        assertThat(expectedAuthor.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(expectedAuthor).usingRecursiveComparison()
                .isEqualTo(actualAuthor);
    }

    @DisplayName("выбрасывать исключение при попытке вставить одного автора два раза")
    @Test
    public void throwsExceptionWhenUniqueAuthorConstraintIsViolated() {
        authorDao.insert(new Author(0, "Васильев", "Владимир"));
        Author again = new Author(0, "Васильев", "Владимир");

        assertThatThrownBy(() -> authorDao.insert(again)).
                as("check unique authors constraints").
                isInstanceOf(RuntimeException.class);
    }

    @DisplayName("возвращать всех авторов из таблицы authors")
    @Test
    public void getAllShouldReturnAllAuthors(){
        List<Author> expectedAuthors = List.of(new Author(0, "Васильев", "Владимир"),
                new Author(0, "Лукьяненко", "Сергей"));

        for (Author author : expectedAuthors) {
            authorDao.insert(author);
        }

        List<Tuple> expectedTuples = new ArrayList<>();
        for (Author author : expectedAuthors) {
            expectedTuples.add(new Tuple(author.getSurname(), author.getName()));
        }

        List<Author> actualAuthors = authorDao.getAll();
        assertThat(actualAuthors).
                extracting("surname", "name").
                as("check that only genres we've just added exist").
                containsExactlyInAnyOrderElementsOf(expectedTuples);
    }

}