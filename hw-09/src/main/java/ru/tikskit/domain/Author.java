package ru.tikskit.domain;

import java.util.Objects;

public class Author {
    private final long id;
    private final String surname;
    private final String name;

    public Author(long id, String surname, String name) {
        this.id = id;
        this.surname = surname;
        this.name = name;
    }

    /**
     * Копирующий конструктор с возможностью задать идентификатор
     * @param id Идентификатор книги в БД
     * @param author Значения всех полей, кроме id, будут взяты из этого объекта
     */
    public Author(long id, Author author) {
        this(id, author.getSurname(), author.getName());
    }

    public long getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", surname='" + surname + "', name='" + name + "'}";
    }
}
