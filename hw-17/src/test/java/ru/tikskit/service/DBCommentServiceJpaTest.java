package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.tikskit.dao.CommentDao;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.BookBuilder;
import ru.tikskit.domain.Comment;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@DisplayName("Репозиторий для комментариев должен")
@DataJpaTest
@Import({DBCommentServiceJpa.class, DBBookServiceJpa.class})
class DBCommentServiceJpaTest {
    @Autowired
    DBCommentService dbCommentService;
    @MockBean
    DBBookService bookService;
    @MockBean
    CommentDao commentDao;

    @DisplayName("добавлять комментарий к книге")
    @Transactional
    @Test
    public void shouldAddCommentForBook() {
        Book darkness = new BookBuilder().
                setBookId(1050L).
                setBookName("Тьма").
                setAuthorId(31).
                setAuthorSurname("Васильев").
                setAuthorName("Владимир").
                setGenreId(100).
                setGenreName("fantasy").
                build();
        when(bookService.getBook(1050L)).thenReturn(Optional.of(darkness));

        dbCommentService.addComment4Book("commentText", 1050L);
        verify(bookService, times(1)).changeBook(darkness);
    }

    @DisplayName("изменять существующий комментарий")
    @Transactional
    @Test
    public void shouldUpdateComment() {
        Comment persisComment = new Comment(10L, "Some text");
        dbCommentService.changeComment(persisComment);
        verify(commentDao, times(1)).save(persisComment);
    }

    @DisplayName("удалять существующий комментарий")
    @Transactional
    @Test
    public void shouldDeleteComment() {
        Comment persisComment = new Comment(10L, "Some text");
        dbCommentService.deleteComment(persisComment);
        verify(commentDao, times(1)).delete(persisComment);
    }
}