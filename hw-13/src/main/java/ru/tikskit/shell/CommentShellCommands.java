package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.CommentRepository;
import ru.tikskit.service.BookService;
import ru.tikskit.service.Output;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentShellCommands {
    private final Output output;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final BookService bookService;

    @ShellMethod(value = "Add comment for book command", key = {"comment add", "c add"})
    public void addComment(String bookName, String authorSurname, String authorName, String commentText) {
        Book book = findBook(bookName, authorSurname, authorName);
        addComment2Book(commentText, book);
        printBookComments(book);
    }

    @ShellMethod(value = "Change comment for book command", key = {"comment change", "c change"})
    public void changeComment(String bookName, String authorSurname, String authorName, String newCommentText,
                              String commentStartsWith, String commentEndsWith) {

        Book book = findBook(bookName, authorSurname, authorName);
        Optional<Comment> comment = findBookComment(book, commentStartsWith, commentEndsWith);

        if (comment.isPresent()) {
            comment.get().setText(newCommentText);
            commentRepository.save(comment.get());
            bookRepository.save(book);
            printBookComments(book);
        }
    }


        @ShellMethod(value = "Delete comment for book command", key = {"comment delete", "c del"})
        public void deleteComment(String bookName, String authorSurname, String authorName, String commentStartsWith,
                                  String commentEndsWith) {
            Book book = findBook(bookName, authorSurname, authorName);
            Optional<Comment> comment = findBookComment(book, commentStartsWith, commentEndsWith);

            if (comment.isPresent()) {
                book.getComments().remove(comment.get());
                commentRepository.delete(comment.get());
                bookRepository.save(book);
                printBookComments(book);
            } else {
                throw new CommentShellException(String.format("Комментарий '%s'...'%s' не найден", commentStartsWith,
                        commentEndsWith));
            }
        }

    @ShellMethod(value = "Show book comments", key = {"show comments", "b comments"})
        public void showComments(String bookName, String authorSurname, String authorName) {
            Book book = findBook(bookName, authorSurname, authorName);
            printBookComments(book);
        }

    private Book findBook(String bookName, String authorSurname, String authorName) {
        Optional<Book> book = bookService.findBook(bookName, authorSurname, authorName);
        if (book.isPresent()) {
            return book.get();
        } else {
            throw new CommentShellException(String.format("Книга %s %s %s не существует", bookName, authorSurname,
                    authorName));
        }
    }

    private void addComment2Book(String commentText, Book book) {
        Comment newComment = commentRepository.save(new Comment(null, commentText));
        List<Comment> comments = book.getComments() == null ? new ArrayList<>() : book.getComments();
        comments.add(newComment);
        book.setComments(comments);
        bookRepository.save(book);
    }

    private void printBookComments(Book book) {
        output.println("Все комментарии к книге:" + System.lineSeparator());
        book.getComments().forEach(output::println);
    }

    private Optional<Comment> findBookComment(Book book, String commentStartsWith, String commentEndsWith) {

        List<Comment> matchingComments = book.getComments().stream().
                filter(s -> s.getText().startsWith(commentStartsWith) && s.getText().endsWith(commentEndsWith)).
                collect(Collectors.toList());
        if (matchingComments.size() == 1) {
            return Optional.of(matchingComments.get(0));
        } else if (matchingComments.size() == 0) {
            output.println(String.format("Нет совпадений по началу '%s' и окончанию '%s'.",
                    commentStartsWith, commentEndsWith));
        } else {
            output.println(String.format("Не удалось однозначно идентифицировать комменатрий по началу '%s' и окончанию '%s'.%s" +
                            "Попробуйте задать более длинные начало и конец для однозначной идентификации",
                    commentStartsWith, commentEndsWith, System.lineSeparator()));
        }
        return Optional.empty();
    }
}
