package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Сервис комментариев должен")
@SpringBootTest
class CommentServiceImplTest {
    private static final String GENRE_NAME = "fantasy";
    private static final String AUTHOR_SURNAME = "Лукьяненко";
    private static final String AUTHOR_NAME = "Сергей";
    public static final String BOOK_NAME = "Смерть";

    @Autowired
    private CommentService commentService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();

        Genre genre = genreRepository.save(new Genre(null, GENRE_NAME));
        Author author = authorRepository.save(new Author(null, AUTHOR_SURNAME, AUTHOR_NAME));
        bookRepository.save(new Book(null, BOOK_NAME, author, genre, null));
    }

    @DisplayName("добавлять коммент для книги")
    @Test
    public void shouldAddCommentToBook() {
        Optional<Book> book = commentService.addComment(BOOK_NAME, AUTHOR_SURNAME, AUTHOR_NAME, "someText");
        assertThat(book).isNotEmpty();
    }

    @DisplayName("возвращать empty, если книга не найдена")
    @Test
    public void AddCommentShouldReturnEmptyIfBookDoesntExist() {
        Optional<Book> book = commentService.addComment("no book", AUTHOR_SURNAME, AUTHOR_NAME, "someText");
        assertThat(book).isEmpty();
    }

    @DisplayName("изменять комментарий у книги")
    @Test
    public void shouldChangeComment() throws AmbiguousCommentsFound, NoMatchingComment {
        commentService.addComment(BOOK_NAME, AUTHOR_SURNAME, AUTHOR_NAME, "initComment");
        Optional<Book> book1 = commentService.changeComment(BOOK_NAME, AUTHOR_SURNAME, AUTHOR_NAME,
                "changedComment", "init", "Comment");
        assertThat(book1).isPresent();
        assertThat(book1.get().getComments()).extracting("text").containsOnly("changedComment");
    }

    @DisplayName("удалять комментарий у книги")
    @Test
    public void shouldDeleteComment() throws AmbiguousCommentsFound, NoMatchingComment {
        commentService.addComment(BOOK_NAME, AUTHOR_SURNAME, AUTHOR_NAME, "initComment");
        Optional<Book> book1 = commentService.deleteComment(BOOK_NAME, AUTHOR_SURNAME, AUTHOR_NAME,
                "init", "Comment");
        assertThat(book1).isPresent();
        assertThat(book1.get().getComments()).extracting("text").doesNotContain("initComment");
    }
}