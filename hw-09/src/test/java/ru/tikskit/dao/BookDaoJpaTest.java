package ru.tikskit.dao;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с книгами должно")
@DataJpaTest
@Import({BookDaoJpa.class, GenreDaoJpa.class, AuthorDaoJpa.class})
class BookDaoJpaTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private GenreDao genreDao;
    @Autowired
    private AuthorDao authorDao;

    @DisplayName("добавлять методом insert одну книгу")
    @Test
    public void insertShouldCreateOneBook() {
        Genre genre = genreDao.insert(new Genre(0, "sci-fi"));
        Author author = authorDao.insert(new Author(0, "Васильев", "Владимир"));
        Book expectedBook = bookDao.insert(new Book(0, "Черная эстафета", genre, author));

        assertThat(expectedBook.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(expectedBook).usingRecursiveComparison()
                .isEqualTo(actualBook);
    }

    @DisplayName("выбрасывать исключение при попытке добавить книгу с нарушением внешнего ключа fk_book_genre")
    @Test
    public void throwsExceptionWhenFKBookGenreIsViolated() {
        Author author = authorDao.insert(new Author(0, "Васильев", "Владимир"));

        Book book = new Book(0, "Черная эстафета", null, author);

        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check fk_book_genre").
                isInstanceOf(Exception.class);
    }

    @DisplayName("выбрасывать исключение при попытке добавить книгу с нарушением внешнего ключа fk_book_author")
    @Test
    public void throwsExceptionWhenFKBookAuthorIsViolated() {
        Genre genre = genreDao.insert(new Genre(0, "sci-fi"));

        Book book = new Book(0, "Черная эстафета", genre, null);

        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check fk_book_author").
                isInstanceOf(Exception.class);
    }

    @DisplayName("выбрасывать исключение при попытке вставить одну и ту же книгу два раза")
    @Test
    public void throwsExceptionWhenUniqueAuthorConstraintIsViolated() {
        Genre genre = genreDao.insert(new Genre(0, "sci-fi"));
        Author author = authorDao.insert(new Author(0, "Васильев", "Владимир"));

        bookDao.insert(new Book(0, "Черная эстафета", genre, author));
        Book again = new Book(0, "Черная эстафета", genre, author);

        assertThatThrownBy(() -> bookDao.insert(again)).
                as("check unique books constraints").
                isInstanceOf(Exception.class);

    }

    @DisplayName("вернуть все книги из таблицы books")
    @Test
    public void shouldReturnAllBooks() {
        Genre sciFi = genreDao.insert(new Genre(0, "sci-fi"));
        Genre fantasy = genreDao.insert(new Genre(0, "fantasy"));

        Author vasilyev = authorDao.insert(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorDao.insert(new Author(0, "Лукьяненко", "Сергей"));

        List<Book> expectedBooks = List.of(
                new Book(0, "Черная эстафета", sciFi, vasilyev),
                new Book(0, "Враг неизвестен", sciFi, vasilyev),
                new Book(0, "Предел", fantasy, lukyanenko)
        );

        expectedBooks = insertAllBooks(expectedBooks);

        List<Tuple> expectedTuples = new ArrayList<>();
        for (Book book : expectedBooks) {
            expectedTuples.add(new Tuple(book.getAuthor().getSurname(), book.getName()));
        }

        em.clear();

        List<Book> actualBooks = bookDao.getAll();
        assertThat(actualBooks).
                extracting("author.surname", "name").
                as("check that only books we've just added exist").
                containsExactlyInAnyOrderElementsOf(expectedTuples);
    }

    private List<Book> insertAllBooks(List<Book> list) {
        List<Book> res = new ArrayList<>();
        for (Book book : list) {
            res.add(bookDao.insert(book));
        }
        return res;
    }

    @DisplayName("фиксировать все изменения книги в БД")
    @Test
    public void updateShouldChangeAllFields() {
        Genre sciFi = genreDao.insert(new Genre(0, "sci-fi"));
        Genre fantasy = genreDao.insert(new Genre(0, "fantasy"));

        Author vasilyev = authorDao.insert(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorDao.insert(new Author(0, "Лукьяненко", "Сергей"));

        Book initBook = bookDao.insert(new Book(0, "Черная эстафета", sciFi, lukyanenko));

        Book updatedBook = new Book(initBook.getId(), "Предел", fantasy, lukyanenko);
        bookDao.update(updatedBook);

        Book actualBook = bookDao.getById(initBook.getId());
        assertThat(actualBook).
                as("check book was updated in DB").
                usingRecursiveComparison().
                isEqualTo(updatedBook);
    }

    @DisplayName("удалять книги из таблицы books")
    @Test
    public void deletedBookShouldDisappearInDB() {
        Genre sciFi = genreDao.insert(new Genre(0, "sci-fi"));
        Genre fantasy = genreDao.insert(new Genre(0, "fantasy"));

        Author vasilyev = authorDao.insert(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorDao.insert(new Author(0, "Лукьяненко", "Сергей"));

        bookDao.insert(new Book(0, "Черная эстафета", sciFi, lukyanenko));
        Book deletedBook = bookDao.insert(new Book(0, "Предел", fantasy, lukyanenko));

        bookDao.deleteById(deletedBook);

        List<Book> actualBooks = bookDao.getAll();

        assertThat(actualBooks).
                as("check deleted book disappeared").
                extracting("id").
                doesNotContain(deletedBook);
    }

    @DisplayName("выбрасывать исключение, если нарушен ключ fk_book_genre")
    @Test
    public void throwsExceptionWhenFKBookGenreViolated() {
        Author vasilyev = authorDao.insert(new Author(0, "Васильев", "Владимир"));

        Book book = new Book(0, "Черная эстафета", null, vasilyev);
        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check exception is throw when fk_book_genre is violated").
                isInstanceOf(Exception.class);

    }

    @DisplayName("выбрасывать исключение, если нарушен ключ fk_book_author")
    @Test
    public void throwsExceptionWhenFKBookAuthorViolated() {
        Genre sciFi = genreDao.insert(new Genre(0, "sci-fi"));

        Book book = new Book(0, "Черная эстафета", sciFi, null);
        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check exception is throw when fk_book_author is violated").
                isInstanceOf(Exception.class);

    }

    @DisplayName("правильно выбирать книги с авторами и жанрами")
    @Test
    public void getAllFullShould() {
        Genre sciFi = genreDao.insert(new Genre(0, "sci-fi"));
        Genre fantasy = genreDao.insert(new Genre(0, "fantasy"));

        Author vasilyev = authorDao.insert(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorDao.insert(new Author(0, "Лукьяненко", "Сергей"));

        Book blackRelay = bookDao.insert(new Book(0, "Черная эстафета", sciFi, vasilyev));
        Book darkness = bookDao.insert(new Book(0, "Тьма", fantasy, lukyanenko));

        List<Book> test = bookDao.getAll();

        List<Book> expected = List.of(new Book(darkness, fantasy, lukyanenko),
                new Book(blackRelay, sciFi, vasilyev));

        List<Book> actual = bookDao.getAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}