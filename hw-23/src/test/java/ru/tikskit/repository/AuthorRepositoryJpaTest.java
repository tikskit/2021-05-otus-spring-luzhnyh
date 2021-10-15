package ru.tikskit.repository;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.tikskit.domain.Author;
import ru.tikskit.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с авторами должно")
@DataJpaTest
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("устанавливать идентификатор объекта при его сохранении в БД")
    @Test
    public void checkSavingAuthorSetsId() {
        Author expected = authorRepository.save(new Author(0, "Васильев", "Владимир"));

        assertThat(expected.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

    }

    @DisplayName("добавлять методом insert одного автора")
    @Test
    public void insertShouldCreateOneAuthor() {
        Author expectedAuthor = authorRepository.save(new Author(0, "Васильев", "Владимир"));

        assertThat(expectedAuthor.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

        Author actualAuthor = em.find(Author.class, expectedAuthor.getId());
        assertThat(expectedAuthor).usingRecursiveComparison()
                .isEqualTo(actualAuthor);
    }

    @DisplayName("возвращать всех авторов из таблицы authors")
    @Test
    public void getAllShouldReturnAllAuthors(){
        List<Author> expectedAuthors = List.of(new Author(0, "Васильев", "Владимир"),
                new Author(0, "Лукьяненко", "Сергей"));
        insertAuthors(expectedAuthors);

        List<Tuple> expectedTuples = new ArrayList<>();
        for (Author author : expectedAuthors) {
            expectedTuples.add(new Tuple(author.getSurname(), author.getName()));
        }

        List<Author> actualAuthors = authorRepository.findAll();
        assertThat(actualAuthors).
                extracting("surname", "name").
                as("check that only genres we've just added exist").
                containsExactlyInAnyOrderElementsOf(expectedTuples);
    }

    private void insertAuthors(List<Author> list) {
        for (Author author : list) {
            em.persist(author);
        }
    }

}