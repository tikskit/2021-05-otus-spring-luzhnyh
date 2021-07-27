package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.service.DBAuthorService;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.DBGenreService;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {
    private final DBBookService dbBookService;
    private final DBAuthorService dbAuthorService;
    private final DBGenreService dbGenreService;
    private final Output output;


    @ShellMethod(value = "Show all books command", key = {"book list", "b list"})
    public void listBooks() {
        List<Book> books = dbBookService.getAll();

        output.println("Current books:");
        books.forEach((b) -> output.println(String.format("\t%s %s \"%s\" (%s)",
                b.getAuthor().getSurname(), b.getAuthor().getName(), b.getName(), b.getGenre().getName())));
    }

    @ShellMethod(value = "Add book command", key = {"book add", "b add"})
    public void addBook(String name, long authorId, long genreId) {
        Optional<Author> author = dbAuthorService.getAuthor(authorId);
        if (author.isEmpty()) {
            throw new BookShellException(String.format("Автор %d не найден в БД", authorId));
        }

        Optional<Genre> genre = dbGenreService.getGenre(genreId);
        if (genre.isEmpty()) {
            throw new BookShellException(String.format("Жанр %d не найден в БД", genreId));
        }


        Book book = dbBookService.addBook(new Book(0, name, author.get(), genre.get(), null));
        output.println(String.format("New book was added: %s", book));
    }


    @ShellMethod(value = "Show book", key = {"book show", "b show"})
    public void showBook(long id) {
        Optional<Book> book = dbBookService.getBook(id);

        output.println(String.format("%d book:%s\t%s", id, System.lineSeparator(), book.orElse(null)));
    }

    @ShellMethod(value = "Delete book", key = {"book del", "b del"})
    public void deleteBook(long id) {
        Optional<Book> book = dbBookService.getBook(id);
        book.ifPresent(dbBookService::deleteBook);
    }

    @ShellMethod(value = "Change book", key = {"book change", "b change"})
    public void changeBook(long id, String name, long authorId, long genreId) {
        Optional<Author> author = dbAuthorService.getAuthor(authorId);
        if (author.isEmpty()) {
            throw new BookShellException(String.format("Автор %d не найден в БД", authorId));
        }

        Optional<Genre> genre = dbGenreService.getGenre(genreId);
        if (genre.isEmpty()) {
            throw new BookShellException(String.format("Жанр %d не найден в БД", genreId));
        }

        dbBookService.changeBook(new Book(id, name, author.get(), genre.get(), null));
    }
}
