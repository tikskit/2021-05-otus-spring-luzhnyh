package ru.tikskit.rest;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.controller.GenreNotFoundException;
import ru.tikskit.domain.Genre;
import ru.tikskit.rest.dto.GenreConverter;
import ru.tikskit.rest.dto.GenreDto;
import ru.tikskit.service.DBGenreService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
public class GenreController {
    private final DBGenreService genreService;
    private final GenreConverter genreConverter;

    @GetMapping(value = "/api/genre/{id}", produces = {"application/hal+json"})
    public RepresentationModel<GenreDto> getGenre(@PathVariable("id") long id) {
        Genre genre = genreService.getGenre(id).orElseThrow(GenreNotFoundException::new);
        GenreDto genreDto = genreConverter.toDto(genre);
        Link link = linkTo(methodOn(GenreController.class).getGenre(id)).withSelfRel();
        genreDto.add(link);
        return genreDto;
    }
}
