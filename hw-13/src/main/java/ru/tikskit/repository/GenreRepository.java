package ru.tikskit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tikskit.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByName(String name);
}
