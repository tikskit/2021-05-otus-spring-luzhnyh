package ru.tikskit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByAuthorAndNameIgnoreCase(Author author, String name);
}
