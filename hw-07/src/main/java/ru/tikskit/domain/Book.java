package ru.tikskit.domain;

import java.util.Objects;

public class Book {
    private long id;
    private final String name;
    private final long genreId;
    private final long authorId;

    public Book(long id, String name, long genreId, long authorId) {
        this.id = id;
        this.name = name;
        this.genreId = genreId;
        this.authorId = authorId;
    }

    public long getGenreId() {
        return genreId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
