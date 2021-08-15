package ru.tikskit.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.GenreRepository;
import ru.tikskit.service.Output;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellCommands {
    private final GenreRepository genreRepository;
    private final Output output;

    @ShellMethod(value = "Show all genres command", key = {"genre list", "g list"})
    public void listGenres() {
        List<Genre> genres = genreRepository.findAll();
        output.println("Current genres:");
        for (Genre g : genres) {
            output.println(String.format("\t%s", g));
        }
    }

    @ShellMethod(value = "Add genre command", key = {"genre add", "g add"})
    public void addGenre(String name) {
        Genre genre = genreRepository.save(new Genre(null, name));
        output.println(String.format("New genre was added: %s", genre));
    }

    @ShellMethod(value = "Show genre", key = {"genre show", "g show"})
    public void showGenre(String name) {
        Optional<Genre> genre = genreRepository.findByName(name);

        output.println(String.format("%s genre:%s\t%s", name, System.lineSeparator(), genre.orElse(null)));
    }
}
