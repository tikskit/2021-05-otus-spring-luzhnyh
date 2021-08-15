package ru.tikskit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для книг должен")
@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository booksRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        booksRepository.deleteAll();
    }

    @DisplayName("сохранять в БД книгу")
    @Test
    public void shouldSaveBook() {
        Optional<Author> lukyanenko = authorRepository.findBySurnameAndName("Лукьяненко", "Сергей");
        Optional<Genre> fantasy = genreRepository.findByName("fantasy");
        assertThat(lukyanenko).isPresent();
        assertThat(fantasy).isPresent();

        List<Comment> comments = commentRepository.saveAll(
                List.of(
                        new Comment(null, "Хорошая книга"),
                        new Comment(null, "Хорошее качество бумаги")
                )
        );

        Book death = new Book(null, "Смерть", lukyanenko.get(), fantasy.get(), comments);
        Book expected = booksRepository.save(death);

        Optional<Book> actual = booksRepository.findById(expected.getId());
        assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("искать книгу по автору и наименованию")
    @Test
    public void shouldFindBookByAuthorAndName() {
        Optional<Author> lukyanenko = authorRepository.findBySurnameAndName("Лукьяненко", "Сергей");
        Optional<Genre> fantasy = genreRepository.findByName("fantasy");
        assertThat(lukyanenko).isPresent();
        assertThat(fantasy).isPresent();

        List<Comment> comments = commentRepository.saveAll(
                List.of(
                        new Comment(null, "Хорошая книга"),
                        new Comment(null, "Хорошее качество бумаги")
                )
        );

        Book death = new Book(null, "Смерть", lukyanenko.get(), fantasy.get(),comments);
        Book expected = booksRepository.save(death);

        Optional<Book> actual = booksRepository.findByAuthorAndNameIgnoreCase(lukyanenko.get(), "смерть");
        assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(expected);
    }
}