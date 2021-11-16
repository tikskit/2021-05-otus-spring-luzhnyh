package ru.tikskit.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthorDto extends RepresentationModel<AuthorDto> {
    private long id;
    private String name;
    private String surname;
}
