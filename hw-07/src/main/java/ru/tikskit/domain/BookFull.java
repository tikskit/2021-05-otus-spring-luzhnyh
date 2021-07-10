package ru.tikskit.domain;

import java.util.Objects;

public class BookFull extends Book {
    private final Author author;
    private final Genre genre;

    public BookFull(long id, String name, Author author, Genre genre) {
        super(id, name, Objects.requireNonNull(author, "author is null").getId(),
                Objects.requireNonNull(genre, "genre is null").getId());

        this.author = author;
        this.genre = genre;
    }

    public BookFull(Book book, Author author, Genre genre) {
        this(book.getId(), book.getName(), author, genre);
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "BookFull{author=" + author + ", genre=" + genre + '}';
    }
}
