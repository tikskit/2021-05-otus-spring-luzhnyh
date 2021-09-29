package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.domain.Genre;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для комментариев должен")
@DataJpaTest
@Import({DBCommentServiceJpa.class, DBBookServiceJpa.class})
class DBCommentServiceJpaTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    DBCommentService dbCommentService;

    private Author lukyanenko;
    private Author vasilyev;

    private Genre sciFi;
    private Genre fantasy;

    private Book blackRelay;
    private Book darkness;

    @BeforeEach
    public void setUp() {
        lukyanenko = em.persist(new Author(0, "Лукьяненко", "Сергей"));
        vasilyev = em.persist(new Author(0, "Васильев", "Сергей"));

        sciFi = em.persist(new Genre(0, "sci-fi"));
        fantasy = em.persist(new Genre(0, "fantasy"));

        blackRelay = em.persist(
                new Book(0, "Черная эстафета", vasilyev, sciFi,
                        List.of(
                                new Comment(0, "Не читал, потому что не нравится"),
                                new Comment(0, "Мало картинок")
                        )));
        darkness = em.persist(new Book(0, "Тьма", lukyanenko, fantasy, null));
    }

    @DisplayName("добавлять комментарий к книге")
    @Transactional
    @Test
    public void shouldAddCommentForBook() {
        final long darknessBookId = darkness.getId();
        final String commentText = "Перечитал 10 раз";
        dbCommentService.addComment4Book(commentText, darknessBookId);

        em.flush();
        em.clear();

        Book darknessActual = em.find(Book.class, darknessBookId);
        assertThat(darknessActual.getComments()).
                extracting("text").
                contains(commentText);
    }

    @DisplayName("изменять существующий комментарий")
    @Transactional
    @Test
    public void shouldUpdateComment() {
        assertThat(blackRelay.getComments()).isNotNull().size().isPositive();
        Comment anyOfBlackRelayComments = blackRelay.getComments().get(0);
        assertThat(anyOfBlackRelayComments.getId()).isPositive();

        final long commentId = anyOfBlackRelayComments.getId();
        final String newText = "Прочитал, понравилось";

        anyOfBlackRelayComments.setText(newText);

        dbCommentService.changeComment(anyOfBlackRelayComments);
        em.flush();
        em.clear();

        Comment comment = em.find(Comment.class, commentId);
        assertThat(comment).extracting("text").isEqualTo(newText);
    }

    @DisplayName("удалять существующий комментарий")
    @Transactional
    @Test
    public void shouldDeleteComment() {
        assertThat(blackRelay.getComments()).isNotNull().size().isPositive();
        Comment anyOfBlackRelayComments = blackRelay.getComments().get(0);
        assertThat(anyOfBlackRelayComments.getId()).isPositive();
        final long commentId = anyOfBlackRelayComments.getId();

        // Удалим blackRelay из контекста, иначе anyOfBlackRelayComments не будет удален
        em.flush();
        em.clear();

        Comment comment = em.find(Comment.class, commentId);
        dbCommentService.deleteComment(comment);
        em.flush();
        em.clear();

        Book book = em.find(Book.class, blackRelay.getId());

        assertThat(book.getComments()).isNotNull().extracting("id").doesNotContain(commentId);
    }
}