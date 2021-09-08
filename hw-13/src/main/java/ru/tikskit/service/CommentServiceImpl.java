package ru.tikskit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Override
    public Optional<Book> addComment(String bookName, String authorSurname, String authorName, String commentText) {
        Optional<Book> book = bookService.findBook(bookName, authorSurname, authorName);
        book.ifPresent(value -> addComment2Book(commentText, value));
        return book;
    }

    @Override
    public Optional<Book> changeComment(String bookName, String authorSurname, String authorName, String newCommentText,
                                        String commentStartsWith, String commentEndsWith)
            throws AmbiguousCommentsFound, NoMatchingComment {

        Optional<Book> book = bookService.findBook(bookName, authorSurname, authorName);

        if (book.isPresent()) {
            Comment comment = findBookComment(book.get(), commentStartsWith, commentEndsWith);

            comment.setText(newCommentText);
            commentRepository.save(comment);
            bookRepository.save(book.get());
        }
        return book;
    }

    @Override
    public Optional<Book> deleteComment(String bookName, String authorSurname, String authorName,
                                        String commentStartsWith, String commentEndsWith)
            throws AmbiguousCommentsFound, NoMatchingComment {
        Optional<Book> book = bookService.findBook(bookName, authorSurname, authorName);

        if (book.isPresent()) {
            Comment comment = findBookComment(book.get(), commentStartsWith, commentEndsWith);
            book.get().getComments().remove(comment);
            commentRepository.delete(comment);
            bookRepository.save(book.get());
        }

        return book;
    }

    private void addComment2Book(String commentText, Book book) {
        Comment newComment = commentRepository.save(new Comment(null, commentText));
        List<Comment> comments = book.getComments() == null ? new ArrayList<>() : book.getComments();
        comments.add(newComment);
        book.setComments(comments);
        bookRepository.save(book);
    }

    private Comment findBookComment(Book book, String commentStartsWith, String commentEndsWith) throws
            NoMatchingComment, AmbiguousCommentsFound {

        List<Comment> matchingComments = book.getComments().stream().
                filter(s -> s.getText().startsWith(commentStartsWith) && s.getText().endsWith(commentEndsWith)).
                collect(Collectors.toList());
        if (matchingComments.size() == 1) {
            return matchingComments.get(0);
        } else if (matchingComments.size() == 0) {
            throw new NoMatchingComment();
        } else {
            throw new AmbiguousCommentsFound();
        }
    }

}
