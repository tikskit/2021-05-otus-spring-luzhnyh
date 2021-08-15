package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Author;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellCommands {
    private final AuthorRepository repository;
    private final Output output;


    @ShellMethod(value = "Show all authors command", key = {"author list", "a list"})
    public void listAuthors() {
        List<Author> authors = repository.findAll();
        output.println("Current authors:");
        for (Author a : authors) {
            output.println(String.format("\t%s", a));
        }
    }

    @ShellMethod(value = "Add author command", key = {"author add", "a add"})
    public void addAuthor(String surname, String name) {
        Author author = repository.save(new Author(null, surname, name));
        output.println(String.format("New author was added: %s", author));
    }

    @ShellMethod(value = "Show author", key = {"author show", "a show"})
    public void showAuthor(String surname, String name) {
        Optional<Author> author = repository.findBySurnameAndName(surname, name);

        output.println(String.format("%s %s author:%s\t%s", surname, name, System.lineSeparator(), author.orElse(null)));
    }
}
