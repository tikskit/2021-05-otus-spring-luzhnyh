package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.GenreRepository;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final Output output;


    @ShellMethod(value = "Show all books command", key = {"book list", "b list"})
    public void listBooks() {
        List<Book> books = bookRepository.findAll();

        output.println("Current books:");
        books.forEach((b) -> output.println(String.format("\t%s %s \"%s\" (%s)",
                b.getAuthor().getSurname(), b.getAuthor().getName(), b.getName(), b.getGenre().getName())));
    }

    @ShellMethod(value = "Add book command", key = {"book add", "b add"})
    @Transactional
    public void addBook(String name, String authorSurname, String authorName, String genreName) {
        Optional<Author> author = findOrCreateAuthor(authorSurname, authorName);
        Optional<Genre> genre = findOrCreateGenre(genreName);

        Book book = bookRepository.save(new Book(null, name, author.get(), genre.get(), null));

        output.println(String.format("New book was added: %s", book));
    }

    @ShellMethod(value = "Show book", key = {"book show", "b show"})
    public void showBook(String name, String authorSurname, String authorName) {
        Optional<Book> book = findBook(name, authorSurname, authorName);

        output.println(book.orElse(null));
    }

    @ShellMethod(value = "Delete book", key = {"book del", "b del"})
    public void deleteBook(String name, String authorSurname, String authorName) {
        Optional<Book> book = findBook(name, authorSurname, authorName);
        book.ifPresent(bookRepository::delete);
        output.println(String.format("Book was deleted: %s", book.orElse(null)));
    }

    @ShellMethod(value = "Change book", key = {"book change", "b change"})
    @Transactional
    public void changeBook(String name, String authorSurname, String authorName,
                           String newName, String newAuthorSurname, String newAuthorName, String newGenreName) {
        Optional<Book> book = findBook(name, authorSurname, authorName);
        if (book.isEmpty()) {
            throw new BookShellException("Книга не найдена в БД");
        }

        Optional<Author> newAuthor = findOrCreateAuthor(newAuthorSurname, newAuthorName);
        Optional<Genre> newGenre = findOrCreateGenre(newGenreName);

        book.get().setName(newName);
        book.get().setAuthor(newAuthor.orElse(null));
        book.get().setGenre(newGenre.orElse(null));

        Book actual = bookRepository.save(book.get());
        output.println(String.format("Book was changed to %s", actual));
    }

    private Optional<Author> findOrCreateAuthor(String newAuthorSurname, String newAuthorName) {
        Optional<Author> newAuthor = authorRepository.findBySurnameAndName(newAuthorSurname, newAuthorName);
        if (newAuthor.isEmpty()) {
            newAuthor = Optional.of(authorRepository.save(new Author(null, newAuthorSurname, newAuthorName)));
        }
        return newAuthor;
    }

    private Optional<Genre> findOrCreateGenre(String genreName) {
        Optional<Genre> genre = genreRepository.findByName(genreName);
        if (genre.isEmpty()) {
            genre = Optional.of(genreRepository.save(new Genre(null, genreName)));
        }
        return genre;
    }

    private Optional<Book> findBook(String name, String authorSurname, String authorName) {
        Optional<Author> author = authorRepository.findBySurnameAndName(authorSurname, authorName);

        if (author.isEmpty()) {
            throw new BookShellException(String.format("Автор %s %s не найден в БД", authorSurname, authorName));
        }

        return bookRepository.findByAuthorAndName(author.get(), name);
    }
}
