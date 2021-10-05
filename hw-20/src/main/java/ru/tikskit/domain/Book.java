package ru.tikskit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Field(name = "name")
    @EqualsAndHashCode.Include
    private String name;

    @Field(name = "author")
    @EqualsAndHashCode.Include
    private Author author;

    @Field(name = "genre")
    private Genre genre;

    @Field(name = "comments")
    private List<Comment> comments;
}
