package ru.tikskit.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookDto extends RepresentationModel<BookDto> {
    private long id;
    @Setter
    private String name;
    @Setter
    private AuthorDto author;
    @Setter
    private GenreDto genre;
}
