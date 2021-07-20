package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Author;
import ru.tikskit.repository.DBAuthorRepository;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellCommands {
    private final DBAuthorRepository dbAuthorRepository;
    private final Output output;


    @ShellMethod(value = "Show all authors command", key = {"author list", "a list"})
    public void listAuthors() {
        List<Author> authors = dbAuthorRepository.getAll();
        output.println("Current authors:");
        for (Author a : authors) {
            output.println(String.format("\t%s", a));
        }
    }

    @ShellMethod(value = "Add author command", key = {"author add", "a add"})
    public void addAuthor(String surname, String name) {
        Author author = dbAuthorRepository.saveAuthor(new Author(0, surname, name));
        output.println(String.format("New author was added: %s", author));
    }

    @ShellMethod(value = "Show author", key = {"author show", "a show"})
    public void showAuthor(long id) {
        Optional<Author> author = dbAuthorRepository.getAuthor(id);

        output.println(String.format("%d author:%s\t%s", id, System.lineSeparator(), author.orElse(null)));
    }
}
