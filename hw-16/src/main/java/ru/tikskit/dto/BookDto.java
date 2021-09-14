package ru.tikskit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookDto {
    private final long id;
    private final String name;
    private final AuthorDto author;
    private final GenreDto genre;
}
