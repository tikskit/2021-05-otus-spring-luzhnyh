package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

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

    private Author lukyanenko;
    private Author vasilyev;
    private Author gaiman;

    private Genre sciFi;
    private Genre fantasy;

    private Book blackRelay;
    private Book darkness;

    @BeforeEach
    public void setUp() {
        lukyanenko = new Author(0, "Лукьяненко", "Сергей");
        dbAuthorService.saveAuthor(lukyanenko);
        vasilyev = new Author(0, "Васильев", "Сергей");
        dbAuthorService.saveAuthor(vasilyev);
        gaiman = new Author(0, "Гейман", "Нил");
        dbAuthorService.saveAuthor(gaiman);

        sciFi = new Genre(0, "sci-fi");
        dbGenreService.saveGenre(sciFi);
        fantasy = new Genre(0, "fantasy");
        dbGenreService.saveGenre(fantasy);

        blackRelay = new Book(0, "Черная эстафета", sciFi.getId(), vasilyev.getId());
        dbBookService.saveBook(blackRelay);
        darkness = new Book(0, "Тьма", fantasy.getId(), lukyanenko.getId());
        dbBookService.saveBook(darkness);
    }

    @DisplayName("добавлять одну и только одну книгу")
    @Test
    public void saveBookShouldAddOnlyOneBook(){

        List<Book> before = dbBookService.getAll();

        Book americanGods = new Book(0, "Американские боги", fantasy.getId(), gaiman.getId());
        dbBookService.saveBook(americanGods);

        List<Book> now = dbBookService.getAll();

        List<Book> expected = new ArrayList<>(before);
        expected.add(americanGods);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("правильно возвращать книгу по идентификатору")
    @Test
    public void getBookShouldReturnProperEntity() {
        Optional<Book> testBlackRelay = dbBookService.getBook(blackRelay.getId());
        assertThat(testBlackRelay.orElseGet(null)).usingRecursiveComparison().isEqualTo(blackRelay);
    }



    @DisplayName("правильно выбирать все книги из таблицы books")
    @Test
    public void getAllShouldReturnAllBooks() {
        List<Book> expected = List.of(darkness, blackRelay);

        List<Book> actual = dbBookService.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("праивльно удалять книги из БД")
    @Test
    public void endityDisappearsWhenItDeleted() {
        List<Book> before = dbBookService.getAll();

        dbBookService.deleteBook(darkness.getId());

        List<Book> after = dbBookService.getAll();

        List<Book> expected = new ArrayList<>(before);
        expected.remove(darkness);

        assertThat(after).containsExactlyInAnyOrderElementsOf(expected);
    }
}