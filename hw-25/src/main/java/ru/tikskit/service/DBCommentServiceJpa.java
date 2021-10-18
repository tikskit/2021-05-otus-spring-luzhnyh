package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.repository.CommentRepository;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DBCommentServiceJpa implements DBCommentService {
    private static final Logger logger = LoggerFactory.getLogger(DBCommentServiceJpa.class);

    private final CommentRepository commentRepository;
    private final DBBookService dbBookService;

    public DBCommentServiceJpa(CommentRepository commentRepository, DBBookService dbBookService) {
        this.commentRepository = commentRepository;
        this.dbBookService = dbBookService;
    }

    @Override
    public Optional<Comment> getComment(long id) {
        Optional<Comment> commentOptional = Optional.of(commentRepository.getById(id));
        logger.info("Comment got from db: {}", commentOptional.get());
        return commentOptional;
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
        Comment updated = commentRepository.save(comment);
        logger.info("Comment updated {}", updated);
        return updated;
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
        logger.info("Comment deleted {}", comment);
    }
}
