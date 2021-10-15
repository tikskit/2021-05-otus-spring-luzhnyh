package ru.tikskit.repository;

import org.junit.jupiter.api.BeforeEach;
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
import ru.tikskit.repository.CommentRepository;
import ru.tikskit.repository.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для комментариев должен")
@DataJpaTest
class CommentRepositoryJpaTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;

    private Author lukyanenko;
    private Author vasilyev;
    private Author gaiman;

    private Genre sciFi;
    private Genre fantasy;

    private Book blackRelay;
    private Book darkness;

    @BeforeEach
    public void setUp() {
        lukyanenko = authorRepository.save(new Author(0, "Лукьяненко", "Сергей"));
        vasilyev = authorRepository.save(new Author(0, "Васильев", "Сергей"));
        gaiman = authorRepository.save(new Author(0, "Гейман", "Нил"));

        sciFi = genreRepository.save(new Genre(0, "sci-fi"));
        fantasy = genreRepository.save(new Genre(0, "fantasy"));

        blackRelay = bookRepository.save(new Book(0, "Черная эстафета", vasilyev, sciFi, null));
        darkness = bookRepository.save(new Book(0, "Тьма", lukyanenko, fantasy, null));

        blackRelay.setComments(List.of(
                new Comment(0, "Не читал, потому что не нравится"),
                new Comment(0, "Мало картинок")
        ));
    }

    @DisplayName("добавлять комментарии")
    @Test
    public void shouldInsertComments() {
        em.flush();
        em.clear();

        Book actualBook = bookRepository.getById(blackRelay.getId());
        assertThat(actualBook).isNotNull().matches(s -> s.getComments().size() == blackRelay.getComments().size());
        assertThat(actualBook.getComments()).containsExactlyInAnyOrderElementsOf(blackRelay.getComments());
    }

    @DisplayName("возвращать коммент по идентификатору")
    @Test
    public void shouldReturnCommentById() {
        em.flush();
        em.clear();

        Comment actual = commentRepository.getById(blackRelay.getComments().get(0).getId());
        assertThat(actual.getId()).isEqualTo(blackRelay.getComments().get(0).getId());
    }

    @DisplayName("изменять комменты в бд")
    @Test
    public void shouldUpdateComment() {
        em.flush();
        em.clear();

        long commentId = blackRelay.getComments().get(0).getId();
        Comment current = commentRepository.getById(commentId);
        current.setText("Прочитал, очень понравилось");
        commentRepository.save(current);

        em.flush();
        em.clear();

        Comment actual = em.find(Comment.class, commentId);
        assertThat(actual.getText()).isEqualToIgnoringCase(current.getText());
    }

    @DisplayName("удалять каменты из БД")
    @Test
    public void shouldDeleteComment() {
        Comment deleting = blackRelay.getComments().get(0);
        long deletingId = deleting.getId();
        commentRepository.delete(deleting);

        em.flush();
        em.clear();

        Comment actual = em.find(Comment.class, deletingId);
        assertThat(actual).isNull();
    }
}