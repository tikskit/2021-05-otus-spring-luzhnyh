package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.dao.CommentDao;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DBCommentServiceJpa implements DBCommentService {
    private static final Logger logger = LoggerFactory.getLogger(DBCommentServiceJpa.class);

    private final CommentDao commentDao;
    private final DBBookService dbBookService;

    public DBCommentServiceJpa(CommentDao commentDao, DBBookService dbBookService) {
        this.commentDao = commentDao;
        this.dbBookService = dbBookService;
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
        Optional<Book> book = dbBookService.getBook(bookId);
        if (book.isPresent()) {
            List<Comment> comments = book.get().getComments() == null ? new ArrayList<>() : book.get().getComments();
            comments.add(new Comment(0, text));
            book.get().setComments(comments);

            dbBookService.changeBook(book.get());
        } else {
            throw new DBCommentServiceException(String.format("Книга %d не найдена", bookId));
        }
    }

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
