package ru.tikskit.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findByAuthorAndNameIgnoreCase(Author author, String name);
    Mono<Book> save(Mono<Book> entity);
}
