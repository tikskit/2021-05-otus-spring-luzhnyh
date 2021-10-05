package ru.tikskit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "authors")
public class Author {
    @Id
    private String id;

    @Field(name = "surname")
    @EqualsAndHashCode.Include
    private String surname;

    @Field(name = "name")
    @EqualsAndHashCode.Include
    private String name;
}
