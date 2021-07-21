package ru.tikskit.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.tikskit.dao.CommentDao;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DBCommentRepositoryJpa implements DBCommentRepository {
    private static final Logger logger = LoggerFactory.getLogger(DBCommentRepositoryJpa.class);

    private final CommentDao commentDao;
    private final DBBookRepository dbBookRepository;

    public DBCommentRepositoryJpa(CommentDao commentDao, DBBookRepository dbBookRepository) {
        this.commentDao = commentDao;
        this.dbBookRepository = dbBookRepository;
    }

    @Override
    public Optional<Comment> getComment(long id) {
        try {
            Optional<Comment> commentOptional = Optional.of(commentDao.getById(id));
            logger.info("Comment got from db: {}", commentOptional.get());
            return commentOptional;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void addComment4Book(String text, long bookId) {
        Optional<Book> book = dbBookRepository.getBook(bookId);
        if (book.isPresent()) {
            List<Comment> comments = book.get().getComments() == null ? new ArrayList<>() : book.get().getComments();
            comments.add(new Comment(0, text));
            book.get().setComments(comments);

            dbBookRepository.changeBook(book.get());
        } else {
            throw new DBCommentRepositoryException(String.format("Книга %d не найдена", bookId));
        }
    }

    @Transactional
    @Override
    public Comment changeComment(Comment comment) {
        try {
            Comment updated = commentDao.update(comment);
            logger.info("Comment updated {}", updated);
            return updated;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteComment(Comment comment) {
        try {
            commentDao.delete(comment);
            logger.info("Comment deleted {}", comment);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
