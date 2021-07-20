package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.DBAuthorRepository;
import ru.tikskit.repository.DBBookRepository;
import ru.tikskit.repository.DBGenreRepository;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {
    private final DBBookRepository dbBookRepository;
    private final DBAuthorRepository dbAuthorRepository;
    private final DBGenreRepository dbGenreRepository;
    private final Output output;


    @ShellMethod(value = "Show all books command", key = {"book list", "b list"})
    public void listBooks() {
        List<Book> books = dbBookRepository.getAll();

        output.println("Current books:");
        books.forEach((b) -> output.println(String.format("\t%s %s \"%s\" (%s)",
                b.getAuthor().getSurname(), b.getAuthor().getName(), b.getName(), b.getGenre().getName())));
    }

    @ShellMethod(value = "Add book command", key = {"book add", "b add"})
    public void addBook(String name, long authorId, long genreId) {
        Optional<Author> author = dbAuthorRepository.getAuthor(authorId);
        if (author.isEmpty()) {
            throw new BookShellException(String.format("Пользователь %d не найден в БД", authorId));
        }

        Optional<Genre> genre = dbGenreRepository.getGenre(genreId);
        if (genre.isEmpty()) {
            throw new BookShellException(String.format("Жанр %d не найден в БД", genreId));
        }

        Book book = dbBookRepository.addBook(new Book(0, name, author.get(), genre.get()));
        output.println(String.format("New book was added: %s", book));
    }


    @ShellMethod(value = "Show book", key = {"book show", "b show"})
    public void showBook(long id) {
        Optional<Book> book = dbBookRepository.getBook(id);

        output.println(String.format("%d book:%s\t%s", id, System.lineSeparator(), book.orElse(null)));
    }

    @ShellMethod(value = "Delete book", key = {"book del", "b del"})
    public void deleteBook(long id) {
        Optional<Book> book = dbBookRepository.getBook(id);
        book.ifPresent(dbBookRepository::deleteBook);
    }

    @ShellMethod(value = "Change book", key = {"book change", "b change"})
    public void changeBook(long id, String name, long authorId, long genreId) {
        Optional<Author> author = dbAuthorRepository.getAuthor(authorId);
        if (author.isEmpty()) {
            throw new BookShellException(String.format("Пользователь %d не найден в БД", authorId));
        }

        Optional<Genre> genre = dbGenreRepository.getGenre(genreId);
        if (genre.isEmpty()) {
            throw new BookShellException(String.format("Жанр %d не найден в БД", genreId));
        }

        dbBookRepository.changeBook(new Book(id, name, author.get(), genre.get()));
    }
}
