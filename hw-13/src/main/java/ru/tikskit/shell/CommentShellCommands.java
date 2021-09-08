package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Book;
import ru.tikskit.service.AmbiguousCommentsFound;
import ru.tikskit.service.BookService;
import ru.tikskit.service.CommentService;
import ru.tikskit.service.NoMatchingComment;
import ru.tikskit.service.Output;

import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class CommentShellCommands {
    private final Output output;
    private final CommentService commentService;
    private final BookService bookService;

    @ShellMethod(value = "Add comment for book command", key = {"comment add", "c add"})
    public void addComment(String bookName, String authorSurname, String authorName, String commentText) {
        Optional<Book> book = commentService.addComment(bookName, authorSurname, authorName, commentText);
        if (book.isPresent()) {
            printBookComments(book.get());
        } else {
            throw new CommentShellException(String.format("Книга %s %s %s не существует", bookName, authorSurname,
                    authorName));
        }
    }

    @ShellMethod(value = "Change comment for book command", key = {"comment change", "c change"})
    public void changeComment(String bookName, String authorSurname, String authorName, String newCommentText,
                              String commentStartsWith, String commentEndsWith) {

        try {
            Optional<Book> book = commentService.changeComment(bookName, authorSurname, authorName, newCommentText,
                    commentStartsWith, commentEndsWith);
            book.ifPresent(this::printBookComments);
        } catch (AmbiguousCommentsFound ambiguousCommentsFound) {
            printAmbiguousCommentsFound(commentStartsWith, commentEndsWith);
        } catch (NoMatchingComment noMatchingComment) {
            printNoMatchingComment(commentStartsWith, commentEndsWith);
        }
    }

    @ShellMethod(value = "Delete comment for book command", key = {"comment delete", "c del"})
    public void deleteComment(String bookName, String authorSurname, String authorName, String commentStartsWith,
                              String commentEndsWith) {
        try {
            Optional<Book> book = commentService.deleteComment(bookName, authorSurname, authorName, commentStartsWith,
                    commentEndsWith);
            book.ifPresent(this::printBookComments);
        } catch (AmbiguousCommentsFound ambiguousCommentsFound) {
            printAmbiguousCommentsFound(commentStartsWith, commentEndsWith);
        } catch (NoMatchingComment noMatchingComment) {
            printNoMatchingComment(commentStartsWith, commentEndsWith);
        }
    }

    @ShellMethod(value = "Show book comments", key = {"show comments", "b comments"})
    public void showComments(String bookName, String authorSurname, String authorName) {
        Optional<Book> book = bookService.findBook(bookName, authorSurname, authorName);
        if (book.isPresent()) {
            printBookComments(book.get());
        } else {
            output.println(String.format("Книга %s автора %s %s не найдена", bookName, authorSurname, authorName));
        }
    }

    private void printBookComments(Book book) {
        output.println("Все комментарии к книге:" + System.lineSeparator());
        book.getComments().forEach(output::println);
    }

    private void printAmbiguousCommentsFound(String commentStartsWith, String commentEndsWith) {
        output.println(
                String.format(
                        "Не удалось однозначно идентифицировать комменатрий по началу '%s' и окончанию '%s'.%s" +
                                "Попробуйте задать более длинные начало и конец для однозначной идентификации",
                        commentStartsWith, commentEndsWith, System.lineSeparator()
                )
        );
    }

    private void printNoMatchingComment(String commentStartsWith, String commentEndsWith) {
        output.println(String.format("Нет совпадений по началу '%s' и окончанию '%s'.", commentStartsWith,
                commentEndsWith));
    }
}
