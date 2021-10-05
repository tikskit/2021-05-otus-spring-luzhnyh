package ru.tikskit.domain;

import java.util.ArrayList;
import java.util.List;

public class BookBuilder {
    private String bookId;
    private String bookName;

    // author
    private String authorId;
    private String authorSurname;
    private String authorName;

    // genre
    private String genreId;
    private String genreName;

    // comments
    List<Comment> comments;

    public BookBuilder setBookId(String bookId) {
        this.bookId = bookId;

        return this;
    }

    public BookBuilder setBookName(String bookName) {
        this.bookName = bookName;

        return this;
    }

    public BookBuilder setAuthorId(String id) {
        this.authorId = id;

        return this;
    }

    public BookBuilder setAuthorSurname(String surname) {
        this.authorSurname = surname;

        return this;
    }

    public BookBuilder setAuthorName(String name) {
        this.authorName = name;

        return this;
    }

    public BookBuilder setGenreId(String id) {
        this.genreId = id;

        return this;
    }

    public BookBuilder setGenreName(String name) {
        this.genreName = name;

        return this;
    }

    public BookBuilder addComment(String id, String text) {
        if (comments == null)
            comments = new ArrayList<>();
        comments.add(new Comment(id, text));

        return  this;
    }

    public Book build() {
        return new Book(bookId, bookName, new Author(authorId, authorSurname, authorName), new Genre(genreId, genreName),
                comments);
    }
}
