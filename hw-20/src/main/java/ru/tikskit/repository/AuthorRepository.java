package ru.tikskit.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.tikskit.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
