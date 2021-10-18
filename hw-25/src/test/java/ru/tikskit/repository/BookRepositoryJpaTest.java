package ru.tikskit.repository;

import org.assertj.core.groups.Tuple;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с книгами должно")
@DataJpaTest
class BookRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("добавлять методом insert одну книгу")
    @Test
    public void insertShouldCreateOneBook() {
        Genre genre = genreRepository.save(new Genre(0, "sci-fi"));
        Author author = authorRepository.save(new Author(0, "Васильев", "Владимир"));
        Book expectedBook = bookRepository.save(new Book(0, "Черная эстафета", author, genre, null));

        assertThat(expectedBook.getId()).
                as("check that id is assigned now").
                isGreaterThan(0);

        Book actualBook = bookRepository.getById(expectedBook.getId());
        assertThat(expectedBook).usingRecursiveComparison()
                .isEqualTo(actualBook);
    }

    @DisplayName("выбрасывать исключение при попытке добавить книгу с нарушением внешнего ключа fk_book_genre")
    @Test
    public void throwsExceptionWhenFKBookGenreIsViolated() {
        Author author = authorRepository.save(new Author(0, "Васильев", "Владимир"));

        Book book = new Book(0, "Черная эстафета", author, null, null);

        assertThatThrownBy(() -> bookRepository.save(book)).
                as("check fk_book_genre").
                isInstanceOf(Exception.class);
    }

    @DisplayName("выбрасывать исключение при попытке добавить книгу с нарушением внешнего ключа fk_book_author")
    @Test
    public void throwsExceptionWhenFKBookAuthorIsViolated() {
        Genre genre = genreRepository.save(new Genre(0, "sci-fi"));

        Book book = new Book(0, "Черная эстафета", null, genre, null);

        assertThatThrownBy(() -> bookRepository.save(book)).
                as("check fk_book_author").
                isInstanceOf(Exception.class);
    }

    @DisplayName("выбрасывать исключение при попытке вставить одну и ту же книгу два раза")
    @Test
    public void throwsExceptionWhenUniqueAuthorConstraintIsViolated() {
        Genre genre = genreRepository.save(new Genre(0, "sci-fi"));
        Author author = authorRepository.save(new Author(0, "Васильев", "Владимир"));

        bookRepository.save(new Book(0, "Черная эстафета", author, genre, null));
        Book again = new Book(0, "Черная эстафета", author, genre, null);

        assertThatThrownBy(() -> bookRepository.save(again)).
                as("check unique books constraints").
                isInstanceOf(Exception.class);

    }

    @DisplayName("вернуть все книги из таблицы books")
    @Test
    public void shouldReturnAllBooks() {
        Genre sciFi = genreRepository.save(new Genre(0, "sci-fi"));
        Genre fantasy = genreRepository.save(new Genre(0, "fantasy"));

        Author vasilyev = authorRepository.save(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorRepository.save(new Author(0, "Лукьяненко", "Сергей"));

        List<Book> expectedBooks = List.of(
                new Book(0, "Черная эстафета", vasilyev, sciFi, null),
                new Book(0, "Враг неизвестен", vasilyev, sciFi, null),
                new Book(0, "Предел", lukyanenko, fantasy, null)
        );

        expectedBooks = insertAllBooks(expectedBooks);

        List<Tuple> expectedTuples = new ArrayList<>();
        for (Book book : expectedBooks) {
            expectedTuples.add(new Tuple(book.getAuthor().getSurname(), book.getName()));
        }

        List<Book> actualBooks = bookRepository.findAll();
        assertThat(actualBooks).
                extracting("author.surname", "name").
                as("check that only books we've just added exist").
                containsExactlyInAnyOrderElementsOf(expectedTuples);
    }

    private List<Book> insertAllBooks(List<Book> list) {
        List<Book> res = new ArrayList<>();
        for (Book book : list) {
            res.add(bookRepository.save(book));
        }
        return res;
    }

    @DisplayName("фиксировать все изменения книги в БД")
    @Test
    public void updateShouldChangeAllFields() {
        Genre sciFi = genreRepository.save(new Genre(0, "sci-fi"));
        Genre fantasy = genreRepository.save(new Genre(0, "fantasy"));

        Author vasilyev = authorRepository.save(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorRepository.save(new Author(0, "Лукьяненко", "Сергей"));

        Book initBook = bookRepository.save(new Book(0, "Черная эстафета", lukyanenko, sciFi, null));

        Book updatedBook = new Book(initBook.getId(), "Предел", lukyanenko, fantasy, null);
        bookRepository.save(updatedBook);

        Book actualBook = bookRepository.getById(initBook.getId());
        assertThat(actualBook).
                as("check book was updated in DB").
                usingRecursiveComparison().
                isEqualTo(updatedBook);
    }

    @DisplayName("удалять книги из таблицы books")
    @Test
    public void deletedBookShouldDisappearInDB() {
        Genre sciFi = genreRepository.save(new Genre(0, "sci-fi"));
        Genre fantasy = genreRepository.save(new Genre(0, "fantasy"));

        Author vasilyev = authorRepository.save(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorRepository.save(new Author(0, "Лукьяненко", "Сергей"));

        bookRepository.save(new Book(0, "Черная эстафета", lukyanenko, sciFi, null));
        Book deletedBook = bookRepository.save(new Book(0, "Предел", lukyanenko, fantasy, null));

        bookRepository.delete(deletedBook);

        List<Book> actualBooks = bookRepository.findAll();

        assertThat(actualBooks).
                as("check deleted book disappeared").
                extracting("id").
                doesNotContain(deletedBook);
    }

    @DisplayName("выбрасывать исключение, если нарушен ключ fk_book_genre")
    @Test
    public void throwsExceptionWhenFKBookGenreViolated() {
        Author vasilyev = authorRepository.save(new Author(0, "Васильев", "Владимир"));

        Book book = new Book(0, "Черная эстафета", vasilyev, null, null);
        assertThatThrownBy(() -> bookRepository.save(book)).
                as("check exception is throw when fk_book_genre is violated").
                isInstanceOf(Exception.class);

    }

    @DisplayName("выбрасывать исключение, если нарушен ключ fk_book_author")
    @Test
    public void throwsExceptionWhenFKBookAuthorViolated() {
        Genre sciFi = genreRepository.save(new Genre(0, "sci-fi"));

        Book book = new Book(0, "Черная эстафета", null, sciFi, null);
        assertThatThrownBy(() -> bookRepository.save(book)).
                as("check exception is throw when fk_book_author is violated").
                isInstanceOf(Exception.class);

    }

    @DisplayName("правильно выбирать книги с авторами и жанрами")
    @Test
    public void getAllShouldReturnBooksWithAuthorsAndGenres() {
        Genre sciFi = genreRepository.save(new Genre(0, "sci-fi"));
        Genre fantasy = genreRepository.save(new Genre(0, "fantasy"));

        Author vasilyev = authorRepository.save(new Author(0, "Васильев", "Владимир"));
        Author lukyanenko = authorRepository.save(new Author(0, "Лукьяненко", "Сергей"));

        Book blackRelay = bookRepository.save(new Book(0, "Черная эстафета", vasilyev, sciFi, null));
        Book darkness = bookRepository.save(new Book(0, "Тьма", lukyanenko, fantasy, null));

        List<Book> expected = List.of(new Book(darkness.getId(), darkness.getName(), lukyanenko, fantasy, null),
                new Book(blackRelay.getId(), blackRelay.getName(), vasilyev, sciFi, null));

        List<Book> actual = bookRepository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("загружать комментарии к книге за один запрос")
    @Test
    public void getBookComments() {
        Genre genre = genreRepository.save(new Genre(0, "sci-fi"));
        Author author = authorRepository.save(new Author(0, "Васильев", "Владимир"));
        Book book = bookRepository.save(new Book(0, "Черная эстафета", author, genre, null));

        List<Comment> expected = List.of(
                new Comment(0, "Лукьяненко чмо"),
                new Comment(0, "Все книги про бабки"),
                new Comment(0, "Как стереть память?"));
        book.setComments(expected);
        em.persist(book);

        em.flush();
        em.clear();

        Book actualBook = em.find(Book.class, book.getId());

        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        assertThat(actualBook.getComments()).
                containsExactlyInAnyOrderElementsOf(book.getComments());

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }
}