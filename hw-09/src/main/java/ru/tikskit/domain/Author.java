package ru.tikskit.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    public Author() {
    }

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

    public void setId(long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
