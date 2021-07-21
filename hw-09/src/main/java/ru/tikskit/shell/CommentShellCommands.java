package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.repository.DBBookRepository;
import ru.tikskit.repository.DBCommentRepository;
import ru.tikskit.service.Output;

import javax.transaction.Transactional;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class CommentShellCommands {
    private final Output output;
    private final DBBookRepository dbBookRepository;
    private final DBCommentRepository dbCommentRepository;

    @Transactional
    @ShellMethod(value = "Add comment for book command", key = {"comment add", "c add"})
    public void addComment(long bookId, String comment) {
        dbCommentRepository.addComment4Book(comment, bookId);
        Optional<Book> book = dbBookRepository.getBook(bookId);
        if (book.isPresent()) {
            output.println("Все комментарии к книге:" + System.lineSeparator());
            book.get().getComments().forEach(c -> output.println(String.format("\t%d. %s", c.getId(), c.getText())));
        } else {
            throw new CommentShellException(String.format("Книга %d не найдена", bookId));
        }
    }

    @Transactional
    @ShellMethod(value = "Change comment for book command", key = {"comment change", "c change"})
    public void changeComment(long commentId, String newText) {
        Optional<Comment> comment = dbCommentRepository.getComment(commentId);

        if (comment.isPresent()) {
            comment.get().setText(newText);
            dbCommentRepository.changeComment(comment.get());
        } else {
            throw new CommentShellException(String.format("Комментарий %d не найден", commentId));
        }
    }

    @Transactional
    @ShellMethod(value = "Delete comment for book command", key = {"comment delete", "c del"})
    public void deleteComment(long commentId) {
        Optional<Comment> comment = dbCommentRepository.getComment(commentId);

        if (comment.isPresent()) {
            dbCommentRepository.deleteComment(comment.get());
        } else {
            throw new CommentShellException(String.format("Комментарий %d не найден", commentId));
        }
    }
}
