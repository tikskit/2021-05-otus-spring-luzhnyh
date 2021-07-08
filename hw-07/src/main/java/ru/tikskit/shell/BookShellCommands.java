package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Book;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {
    private final DBBookService dbBookService;
    private final Output output;


    @ShellMethod(value = "Show all books command", key = {"book list", "b list"})
    public void listBooks() {
        List<Book> books = dbBookService.getAll();
        output.println("Current books:");
        for (Book b : books) {
            output.println(String.format("\t%s", b));
        }
    }

    @ShellMethod(value = "Add book command", key = {"book add", "b add"})
    public void addBook(String name, long genreId, long authorId) {
        Book book = new Book(0, name, genreId, authorId);
        dbBookService.addBook(book);
        output.println(String.format("New book was added: %s", book));
    }


    @ShellMethod(value = "Show book", key = {"book show", "b show"})
    public void showBook(long id) {
        Optional<Book> book = dbBookService.getBook(id);

        output.println(String.format("%d book:%s\t%s", id, System.lineSeparator(), book.orElse(null)));
    }

    @ShellMethod(value = "Delete book", key = {"book del", "b del"})
    public void deleteBook(long id) {
        dbBookService.deleteBook(id);
    }

    @ShellMethod(value = "Change book", key = {"book change", "b change"})
    public void changeBook(long id, String name, long genreId, long authorId) {
        dbBookService.changeBook(new Book(id, name, genreId, authorId));
    }
}
