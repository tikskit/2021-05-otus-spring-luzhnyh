package ru.tikskit.dao;

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

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для комментариев должен")
@DataJpaTest
@Import({BookDaoJpa.class})
class CommentDaoJpaTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private GenreDao genreDao;
    @Autowired
    private BookDao bookDao;

    private Author lukyanenko;
    private Author vasilyev;
    private Author gaiman;

    private Genre sciFi;
    private Genre fantasy;

    private Book blackRelay;
    private Book darkness;

    @BeforeEach
    public void setUp() {
        lukyanenko = authorDao.save(new Author(0, "Лукьяненко", "Сергей"));
        vasilyev = authorDao.save(new Author(0, "Васильев", "Сергей"));
        gaiman = authorDao.save(new Author(0, "Гейман", "Нил"));

        sciFi = genreDao.save(new Genre(0, "sci-fi"));
        fantasy = genreDao.save(new Genre(0, "fantasy"));

        blackRelay = bookDao.insert(new Book(0, "Черная эстафета", vasilyev, sciFi, null));
        darkness = bookDao.insert(new Book(0, "Тьма", lukyanenko, fantasy, null));

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

        Book actualBook = bookDao.getById(blackRelay.getId());
        assertThat(actualBook).isNotNull().matches(s -> s.getComments().size() == blackRelay.getComments().size());
        assertThat(actualBook.getComments()).containsExactlyInAnyOrderElementsOf(blackRelay.getComments());
    }

    @DisplayName("возвращать коммент по идентификатору")
    @Test
    public void shouldReturnCommentById() {
        em.flush();
        em.clear();

        Comment actual = commentDao.getById(blackRelay.getComments().get(0).getId());
        assertThat(actual.getId()).isEqualTo(blackRelay.getComments().get(0).getId());
    }

    @DisplayName("изменять комменты в бд")
    @Test
    public void shouldUpdateComment() {
        em.flush();
        em.clear();

        long commentId = blackRelay.getComments().get(0).getId();
        Comment current = commentDao.getById(commentId);
        current.setText("Прочитал, очень понравилось");
        commentDao.save(current);

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
        commentDao.delete(deleting);

        em.flush();
        em.clear();

        Comment actual = em.find(Comment.class, deletingId);
        assertThat(actual).isNull();
    }
}