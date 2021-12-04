package ru.tikskit.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class DBCommentServiceJpa implements DBCommentService {
    private static final Logger logger = LoggerFactory.getLogger(DBCommentServiceJpa.class);

    private final CommentDao commentDao;
    private final DBBookService dbBookService;

    @Override
    @HystrixCommand(commandKey = "getCommentKey", fallbackMethod = "getCommentStub")
    public Optional<Comment> getComment(long id) {
        Optional<Comment> commentOptional = commentDao.findById(id);
        logger.info("Comment got from db: {}", commentOptional.get());
        return commentOptional;
    }

    public Optional<Comment> getCommentStub(long id) {
        return Optional.of(new Comment(0L, "N/A"));
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
        Comment updated = commentDao.save(comment);
        logger.info("Comment updated {}", updated);
        return updated;
    }

    @Override
    public void deleteComment(Comment comment) {
        commentDao.delete(comment);
        logger.info("Comment deleted {}", comment);
    }
}
