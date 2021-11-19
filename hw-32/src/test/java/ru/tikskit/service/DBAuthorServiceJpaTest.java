package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.domain.Author;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@DisplayName("Сервис для работы с авторами должен")
@DataJpaTest
@Import({DBAuthorServiceJpa.class})
class DBAuthorServiceJpaTest {

    @Autowired
    private DBAuthorService authorService;
    @MockBean
    private AuthorDao authorDao;

    @DisplayName("добавлять одного и только одного автора")
    @Test
    public void saveAuthorShouldInsertOneAuthor() {
        Author newAuthor = new Author(0, "Гейман", "Нил");

        Author persistAuthor = new Author(1, newAuthor.getSurname(), newAuthor.getName());
        when(authorDao.save(newAuthor)).thenReturn(persistAuthor);

        authorService.saveAuthor(newAuthor);

        verify(authorDao, times(1)).save(newAuthor);
    }

    @DisplayName("правильно возвращать автора по идентификатору")
    @Test
    public void getAuthorShouldReturnProperEntity() {
        when(authorDao.getById(10L)).thenReturn(new Author(10, "Лукьяненко", "Сергей"));
        authorService.getAuthor(10);
        verify(authorDao, times(1)).getById(10L);
    }

    @DisplayName("правильно выбирать всех авторов из таблицы authors")
    @Test
    public void getAllShouldReturnAllBooks() {
        authorService.getAll();
        verify(authorDao, times(1)).findAll();
    }
}