package ru.tikskit.domain.tar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "authors")
public class Author {
    @Id
    private String id;

    @Field(name = "surname")
    private String surname;

    @Field(name = "name")
    private String name;
}
