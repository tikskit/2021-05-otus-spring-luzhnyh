package ru.tikskit.dao;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с книгами должно")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {
    @Autowired
    private BookDaoJdbc bookDao;
    @Autowired
    private GenreDaoJdbc genreDao;
    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("добавлять методом insert одну книгу")
    @Test
    public void insertShouldCreateOneBook() {
        Genre genre = new Genre(0, "sci-fi");
        genreDao.insert(genre);

        Author author = new Author(0, "Васильев", "Владимир");
        authorDao.insert(author);

        Book expectedBook = new Book(0, "Черная эстафета", genre.getId(), author.getId());
        bookDao.insert(expectedBook);

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
        Author author = new Author(0, "Васильев", "Владимир");
        authorDao.insert(author);

        Book book = new Book(0, "Черная эстафета", 100500, author.getId());

        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check fk_book_genre").
                isInstanceOf(Exception.class);
    }

    @DisplayName("выбрасывать исключение при попытке добавить книгу с нарушением внешнего ключа fk_book_author")
    @Test
    public void throwsExceptionWhenFKBookAuthorIsViolated() {
        Genre genre = new Genre(0, "sci-fi");
        genreDao.insert(genre);

        Book book = new Book(0, "Черная эстафета", genre.getId(), 100500);

        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check fk_book_author").
                isInstanceOf(Exception.class);
    }

    @DisplayName("выбрасывать исключение при попытке вставить одну и ту же книгу два раза")
    @Test
    public void throwsExceptionWhenUniqueAuthorConstraintIsViolated() {
        Genre genre = new Genre(0, "sci-fi");
        genreDao.insert(genre);

        Author author = new Author(0, "Васильев", "Владимир");
        authorDao.insert(author);


        bookDao.insert(new Book(0, "Черная эстафета", genre.getId(), author.getId()));
        Book again = new Book(0, "Черная эстафета", genre.getId(), author.getId());

        assertThatThrownBy(() -> bookDao.insert(again)).
                as("check unique books constraints").
                isInstanceOf(Exception.class);

    }

    @DisplayName("вернуть все книги из таблицы books")
    @Test
    public void shouldReturnAllBooks() {
        Genre sciFi = new Genre(0, "sci-fi");
        genreDao.insert(sciFi);

        Genre fantasy = new Genre(0, "fantasy");
        genreDao.insert(fantasy);

        Author vasilyev = new Author(0, "Васильев", "Владимир");
        authorDao.insert(vasilyev);

        Author lukyanenko = new Author(0, "Лукьяненко", "Сергей");
        authorDao.insert(lukyanenko);

        List<Book> expectedBooks = List.of(
                new Book(0, "Черная эстафета", sciFi.getId(), vasilyev.getId()),
                new Book(0, "Враг неизвестен", sciFi.getId(), vasilyev.getId()),
                new Book(0, "Предел", fantasy.getId(), lukyanenko.getId())
        );

        for (Book book : expectedBooks) {
            bookDao.insert(book);
        }

        List<Tuple> expectedTuples = new ArrayList<>();
        for (Book book : expectedBooks) {
            expectedTuples.add(new Tuple(book.getAuthorId(), book.getName()));
        }

        List<Book> actualBooks = bookDao.getAll();
        assertThat(actualBooks).
                extracting("authorId", "name").
                as("check that only books we've just added exist").
                containsExactlyInAnyOrderElementsOf(expectedTuples);
    }


    @DisplayName("фиксировать все изменения книги в БД")
    @Test
    public void updateShouldChangeAllFields() {
        Genre sciFi = new Genre(0, "sci-fi");
        genreDao.insert(sciFi);

        Genre fantasy = new Genre(0, "fantasy");
        genreDao.insert(fantasy);

        Author vasilyev = new Author(0, "Васильев", "Владимир");
        authorDao.insert(vasilyev);

        Author lukyanenko = new Author(0, "Лукьяненко", "Сергей");
        authorDao.insert(lukyanenko);

        Book initBook = new Book(0, "Черная эстафета", sciFi.getId(), lukyanenko.getId());
        bookDao.insert(initBook);

        Book updatedBook = new Book(initBook.getId(), "Предел", fantasy.getId(), lukyanenko.getId());
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
        Genre sciFi = new Genre(0, "sci-fi");
        genreDao.insert(sciFi);

        Genre fantasy = new Genre(0, "fantasy");
        genreDao.insert(fantasy);

        Author vasilyev = new Author(0, "Васильев", "Владимир");
        authorDao.insert(vasilyev);

        Author lukyanenko = new Author(0, "Лукьяненко", "Сергей");
        authorDao.insert(lukyanenko);

        Book book1 = new Book(0, "Черная эстафета", sciFi.getId(), lukyanenko.getId());
        bookDao.insert(book1);

        Book deletedBook = new Book(0, "Предел", fantasy.getId(), lukyanenko.getId());
        bookDao.insert(deletedBook);

        bookDao.deleteById(deletedBook.getId());

        List<Book> actualBooks = bookDao.getAll();


        assertThat(actualBooks).
                as("check deleted book disappeared").
                extracting("id").
                doesNotContain(deletedBook);
    }

    @DisplayName("выбрасывать исключение, если нарушен ключ fk_book_genre")
    @Test
    public void throwsExceptionWhenFKBookGenreViolated() {

        Author vasilyev = new Author(0, "Васильев", "Владимир");
        authorDao.insert(vasilyev);

        Book book = new Book(0, "Черная эстафета", 0, vasilyev.getId());
        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check exception is throw when fk_book_genre is violated").
                isInstanceOf(Exception.class);

    }

    @DisplayName("выбрасывать исключение, если нарушен ключ fk_book_author")
    @Test
    public void throwsExceptionWhenFKBookAuthorViolated() {

        Genre sciFi = new Genre(0, "sci-fi");
        genreDao.insert(sciFi);

        Book book = new Book(0, "Черная эстафета", sciFi.getId(), 0);
        assertThatThrownBy(() -> bookDao.insert(book)).
                as("check exception is throw when fk_book_author is violated").
                isInstanceOf(Exception.class);

    }

}