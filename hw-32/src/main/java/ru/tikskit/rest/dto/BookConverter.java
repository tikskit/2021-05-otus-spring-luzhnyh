package ru.tikskit.rest.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tikskit.domain.Book;

@Component
@AllArgsConstructor
public class BookConverter {
    private final AuthorConverter authorConverter;
    private final GenreConverter genreConverter;

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getName(),
                authorConverter.toDto(book.getAuthor()),
                genreConverter.toDto(book.getGenre())
        );
    }

    public Book toBook(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getName(),
                authorConverter.toAuthor(bookDto.getAuthor()),
                genreConverter.toGenre(bookDto.getGenre()),
                null);
    }
}
