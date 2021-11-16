package ru.tikskit.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GenreDto extends RepresentationModel<GenreDto> {
    private long id;
    private String name;
}
