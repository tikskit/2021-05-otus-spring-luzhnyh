package ru.tikskit.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tikskit.domain.Book;

import javax.persistence.EntityNotFoundException;

@Component
@AllArgsConstructor
public class BookConverter {

    private final AuthorConverter authorConverter;
    private final GenreConverter genreConverter;

    public BookDto toDto(Book book) {
        AuthorDto authorDto;
        try {
            authorDto = authorConverter.toDto(book.getAuthor());
        } catch (EntityNotFoundException e) {
            throw new BookNoAuthorException();
        }

        GenreDto genreDto;
        try {
            genreDto = genreConverter.toDto(book.getGenre());
        } catch (EntityNotFoundException e) {
            throw new BookNoGenreException();
        }

        return new BookDto(book.getId(), book.getName(), authorDto, genreDto);
    }
}
