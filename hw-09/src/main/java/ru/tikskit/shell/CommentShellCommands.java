package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.DBCommentService;
import ru.tikskit.service.Output;

import javax.transaction.Transactional;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class CommentShellCommands {
    private final Output output;
    private final DBBookService dbBookService;
    private final DBCommentService dbCommentService;

    @Transactional
    @ShellMethod(value = "Add comment for book command", key = {"comment add", "c add"})
    public void addComment(long bookId, String comment) {
        dbCommentService.addComment4Book(comment, bookId);
        Optional<Book> book = dbBookService.getBook(bookId);
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
        Optional<Comment> comment = dbCommentService.getComment(commentId);

        if (comment.isPresent()) {
            comment.get().setText(newText);
            dbCommentService.changeComment(comment.get());
        } else {
            throw new CommentShellException(String.format("Комментарий %d не найден", commentId));
        }
    }

    @Transactional
    @ShellMethod(value = "Delete comment for book command", key = {"comment delete", "c del"})
    public void deleteComment(long commentId) {
        Optional<Comment> comment = dbCommentService.getComment(commentId);

        if (comment.isPresent()) {
            dbCommentService.deleteComment(comment.get());
        } else {
            throw new CommentShellException(String.format("Комментарий %d не найден", commentId));
        }
    }
}
