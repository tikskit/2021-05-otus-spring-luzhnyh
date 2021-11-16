package ru.tikskit.rest;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.controller.AuthorNotFoundException;
import ru.tikskit.domain.Author;
import ru.tikskit.rest.dto.AuthorConverter;
import ru.tikskit.rest.dto.AuthorDto;
import ru.tikskit.rest.dto.BookConverter;
import ru.tikskit.rest.dto.BookDto;
import ru.tikskit.service.DBAuthorService;
import ru.tikskit.service.DBBookService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
public class AuthorController {
    private final DBAuthorService authorService;
    private final AuthorConverter authorConverter;
    private final DBBookService bookService;
    private final BookConverter bookConverter;

    @GetMapping(value = "/api/author/{id}", produces = {"application/hal+json"})
    public RepresentationModel<AuthorDto> getAuthor(@PathVariable("id") long id) {
        Author author = authorService.getAuthor(id).orElseThrow(AuthorNotFoundException::new);
        AuthorDto authorDto = authorConverter.toDto(author);
        List<BookDto> books = bookService.getByAuthor(author).stream().map(bookConverter::toDto)
                .collect(Collectors.toList());

        for (BookDto b : books) {
            Link bookLink = linkTo(methodOn(BookController.class).getBook(b.getId())).withRel("books");
            authorDto.add(bookLink);
        }

        Link link = linkTo(methodOn(AuthorController.class).getAuthor(id)).withSelfRel();
        authorDto.add(link);
        return authorDto;
    }
}
