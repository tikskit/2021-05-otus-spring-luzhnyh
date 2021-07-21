package ru.tikskit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.AuthorDaoJpa;
import ru.tikskit.dao.BookDaoJpa;
import ru.tikskit.dao.GenreDaoJpa;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.domain.Genre;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для книг должен")
@DataJpaTest
@Import({DBBookRepositoryJpa.class, BookDaoJpa.class, DBAuthorRepositoryJpa.class, AuthorDaoJpa.class,
        DBGenreRepositoryJpa.class, GenreDaoJpa.class})
class DBBookRepositoryJpaTest {
    @Autowired
    DBBookRepository dbBookRepository;
    @Autowired
    DBAuthorRepository dbAuthorRepository;
    @Autowired
    DBGenreRepository dbGenreRepository;
    @Autowired
    TestEntityManager em;

    private Author lukyanenko;
    private Author vasilyev;
    private Author gaiman;

    private Genre sciFi;
    private Genre fantasy;

    private Book blackRelay;
    private Book darkness;

    @BeforeEach
    public void setUp() {
        lukyanenko = dbAuthorRepository.saveAuthor(new Author(0, "Лукьяненко", "Сергей"));
        vasilyev = dbAuthorRepository.saveAuthor(new Author(0, "Васильев", "Сергей"));
        gaiman = dbAuthorRepository.saveAuthor(new Author(0, "Гейман", "Нил"));

        sciFi = dbGenreRepository.saveGenre(new Genre(0, "sci-fi"));
        fantasy = dbGenreRepository.saveGenre(new Genre(0, "fantasy"));

        blackRelay = dbBookRepository.addBook(new Book(0, "Черная эстафета", vasilyev, sciFi, null));
        darkness = dbBookRepository.addBook(new Book(0, "Тьма", lukyanenko, fantasy, null));
    }

    @DisplayName("добавлять одну и только одну книгу")
    @Test
    public void addBookShouldAddOnlyOneBook(){

        List<Book> before = dbBookRepository.getAll();

        Book americanGods = dbBookRepository.addBook(new Book(0, "Американские боги", gaiman, fantasy, null));

        List<Book> now = dbBookRepository.getAll();

        List<Book> expected = new ArrayList<>(before);
        expected.add(americanGods);

        assertThat(now).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("Добавлять новую книгу с объектами автор и жанр, которые не в состоянии persistent")
    @Test
    public void shouldAddNewBookWithTransientOrDatachedAuthorAndGenre() {
        Author author = new Author(0, "Шилдт", "Герберт");
        Genre genre = new Genre(0, "Computer science");
        Book book = new Book(0, "Полный Самоучитель С++", author, genre, null);

        dbBookRepository.addBook(book);
    }

    @DisplayName("правильно возвращать книгу по идентификатору")
    @Test
    public void getBookShouldReturnProperEntity() {
        Optional<Book> testBlackRelay = dbBookRepository.getBook(blackRelay.getId());
        assertThat(testBlackRelay.orElseGet(null)).usingRecursiveComparison().isEqualTo(blackRelay);
    }

    @DisplayName("правильно выбирать все книги из таблицы books")
    @Test
    public void getAllShouldReturnAllBooks() {
        List<Book> expected = List.of(darkness, blackRelay);

        List<Book> actual = dbBookRepository.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("правильно удалять книги из БД")
    @Test
    public void entityDisappearsWhenItDeleted() {
        List<Book> before = dbBookRepository.getAll();

        dbBookRepository.deleteBook(darkness);

        List<Book> after = dbBookRepository.getAll();

        List<Book> expected = new ArrayList<>(before);
        expected.remove(darkness);

        assertThat(after).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("правильно изменять книги в БД")
    @Test
    public void bookChangedProperly() {
        Book blackRelayChanged = new Book(blackRelay.getId(), "Очень черная эстафета", lukyanenko, fantasy,
                null);

        dbBookRepository.changeBook(blackRelayChanged);

        Optional<Book> blackRelayActual = dbBookRepository.getBook(blackRelay.getId());
        assertThat(blackRelayActual.orElseGet(null)).usingRecursiveComparison().isEqualTo(blackRelayChanged);
    }

    @DisplayName("Должно правильно выбирать расширенные книги")
    @Test
    public void getFullShouldReturnExtendedBooks() {
        List<Book> books = dbBookRepository.getAll();
        Book darkness = new Book(this.darkness.getId(), this.darkness.getName(), lukyanenko, fantasy, null);
        Book blackRelay = new Book(this.blackRelay.getId(), this.blackRelay.getName(), vasilyev, sciFi, null);

        assertThat(books).containsExactlyInAnyOrderElementsOf(List.of(darkness, blackRelay));
    }

    @DisplayName("загружать лениво каменты к книге. Один запрос на все каменты")
    @Test
    public void shouldReturnBookThatPullsCommentsLazyly() {
        Book book = new Book(0, "test", lukyanenko, sciFi, null);

        List<Comment> expected = List.of(
                new Comment(0, "Лукьяненко чмо"),
                new Comment(0, "Все книги про бабки"),
                new Comment(0, "Перечитываю 10 раз"),
                new Comment(0, "Он целый год писал, а я за ночь прочитал"),
                new Comment(0, "Хорошая книга, рояль на ней стоит и не шатается"),
                new Comment(0, "Качество бумаги не очень, газетка"),
                new Comment(0, "Как стереть память?"));
        book.setComments(expected);

        book = dbBookRepository.addBook(book);
        long bookId = book.getId();

        em.flush();
        em.clear();

        Optional<Book> actual = dbBookRepository.getBook(bookId);

        assertThat(actual).isPresent();
        assertThat(actual.get().getComments()).containsExactlyInAnyOrderElementsOf(book.getComments());

    }

    @DisplayName("сохранять в БД все добавленные комментарии к существующей книге")
    @Transactional
    @Test
    public void shouldAddComments2ExistingBook() {
        List<Comment> initComments = new ArrayList<>();
        initComments.add(new Comment(0, "Лукьяненко чмо"));
        initComments.add(new Comment(0, "Все книги про бабки"));
        initComments.add(new Comment(0, "Как стереть память?"));

        darkness.setComments(initComments);
        Book updatedBook = dbBookRepository.changeBook(darkness);
        final long bookId = updatedBook.getId();

        em.flush();
        em.clear();

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook.getComments()).containsExactlyInAnyOrderElementsOf(initComments);
    }

}