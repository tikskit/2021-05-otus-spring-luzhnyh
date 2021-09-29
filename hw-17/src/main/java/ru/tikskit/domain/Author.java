package ru.tikskit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors", uniqueConstraints = @UniqueConstraint(columnNames = {"surname", "name"}))
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    /**
     * Копирующий конструктор с возможностью задать идентификатор
     * @param id Идентификатор книги в БД
     * @param author Значения всех полей, кроме id, будут взяты из этого объекта
     */
    public Author(long id, Author author) {
        this(id, author.getSurname(), author.getName());
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", surname='" + surname + "', name='" + name + "'}";
    }
}
