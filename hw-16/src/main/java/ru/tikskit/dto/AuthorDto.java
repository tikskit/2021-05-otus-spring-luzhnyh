package ru.tikskit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorDto {
    private final long id;
    private final String surname;
    private final String name;
}
