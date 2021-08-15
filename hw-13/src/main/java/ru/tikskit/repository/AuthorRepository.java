package ru.tikskit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tikskit.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {

    Optional<Author> findBySurnameAndName(String surname, String name);
}
