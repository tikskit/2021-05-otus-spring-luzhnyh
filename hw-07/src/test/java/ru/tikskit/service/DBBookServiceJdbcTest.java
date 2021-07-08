package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.AuthorDaoJdbc;
import ru.tikskit.dao.BookDaoJdbc;
import ru.tikskit.dao.GenreDaoJdbc;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Сервис для книг должен")
@JdbcTest
@Import({DBBookServiceJdbc.class, BookDaoJdbc.class, DBAuthorServiceJdbc.class, AuthorDaoJdbc.class,
        DBGenreServiceJdbc.class, GenreDaoJdbc.class})
class DBBookServiceJdbcTest {
    @Autowired
    DBBookService dbBookService;
    @Autowired
    DBAuthorService dbAuthorService;
    @Autowired
    DBGenreService dbGenreService;

    @DisplayName("добавлять одну и только одну книгу")
    @Test
    public void saveBookShouldAddOnlyOneBook(){
        Author lukyanenko = new Author(0, "Лукьяненко", "Сергей");
        dbAuthorService.saveAuthor(lukyanenko);
        Author vasilyev = new Author(0, "Васильев", "Сергей");
        dbAuthorService.saveAuthor(vasilyev);
        Author gaiman = new Author(0, "Гейман", "Нил");
        dbAuthorService.saveAuthor(gaiman);

        Genre sciFi = new Genre(0, "sci-fi");
        dbGenreService.saveGenre(sciFi);
        Genre fantasy = new Genre(0, "fantasy");
        dbGenreService.saveGenre(fantasy);

        Book blackRelay = new Book(0, "Черная эстафета", sciFi.getId(), vasilyev.getId());
        dbBookService.saveBook(blackRelay);
        Book darkness = new Book(0, "Тьма", fantasy.getId(), lukyanenko.getId());
        dbBookService.saveBook(darkness);

        List<Book> before = dbBookService.getAll();

        Book americanGods = new Book(0, "Американские боги", fantasy.getId(), gaiman.getId());
        dbBookService.saveBook(americanGods);

        List<Book> now = dbBookService.getAll();

        List<Book> expected = new ArrayList<>(before);
        expected.add(americanGods);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }
}