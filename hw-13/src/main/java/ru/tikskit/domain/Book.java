package ru.tikskit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "author")
    private Author author;

    @Field(name = "genre")
    private Genre genre;

    @Field(name = "comments")
    private List<Comment> comments;
}
