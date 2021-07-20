package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.DBGenreRepository;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellCommands {
    private final DBGenreRepository dbGenreRepository;
    private final Output output;

    @ShellMethod(value = "Show all genres command", key = {"genre list", "g list"})
    public void listGenres() {
        List<Genre> genres = dbGenreRepository.getAll();
        output.println("Current genres:");
        for (Genre g : genres) {
            output.println(String.format("\t%s", g));
        }
    }

    @ShellMethod(value = "Add genre command", key = {"genre add", "g add"})
    public void addGenre(String name) {
        Genre genre = dbGenreRepository.saveGenre(new Genre(0, name));
        output.println(String.format("New genre was added: %s", genre));
    }

    @ShellMethod(value = "Show genre", key = {"genre show", "g show"})
    public void showGenre(long id) {
        Optional<Genre> genre = dbGenreRepository.getGenre(id);

        output.println(String.format("%d genre:%s\t%s", id, System.lineSeparator(), genre.orElse(null)));
    }
}
