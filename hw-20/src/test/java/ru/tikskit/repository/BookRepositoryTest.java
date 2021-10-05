package ru.tikskit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Репозиторий книг должен")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
        assertNotNull(mongoTemplate, "Mongo template isn't injected!");
        bookRepository.deleteAll().block();
    }

    @DisplayName("Присваивать идентификатор для создаваемой книги")
    @Test
    public void shouldSetBookIdOnSave() {
        Mono<Book> bookMono = genreRepository.findByName("fantasy")
                .zipWith(authorRepository.findBySurnameAndName("Лукьяненко", "Сергей"))
                .map(objects -> {
                    return new Book(null, "The darkness", objects.getT2(), objects.getT1(), null);
                })
                .flatMap(bookRepository::save);
        StepVerifier
                .create(bookMono)
                .assertNext(book -> assertNotNull(book.getId()))
                .expectComplete()
                .verify();
    }

    @DisplayName("Сохраняет комментарий вместе с книгой")
    @Test
    public void shouldSetIdToNewBooksComment() {
        Book book = genreRepository.findByName("fantasy")
                .zipWith(authorRepository.findBySurnameAndName("Лукьяненко", "Сергей"))

                .map(objects -> {
                    Book book1 = new Book(null, "The darkness", objects.getT2(), objects.getT1(), null);
                    List<Comment> comments = new ArrayList<>();
                    comments.add(new Comment(null, "test"));
                    book1.setComments(comments);
                    return book1;
                })
                .flatMap(bookRepository::save)
                .block();

        Mono<Book> byId = bookRepository.findById(book.getId());
        StepVerifier
                .create(byId)
                .assertNext(b -> {
                    assertNotNull(b.getComments());
                    assertEquals(b.getComments().size(), 1);
                    assertEquals(b.getComments().get(0).getText(), "test");
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("Удаляет книгу по идентификатору")
    @Test
    public void shouldDeleteBookById() {
        Book book = genreRepository.findByName("fantasy")
                .zipWith(authorRepository.findBySurnameAndName("Лукьяненко", "Сергей"))

                .map(objects -> {
                    Book book1 = new Book(null, "The darkness", objects.getT2(), objects.getT1(), null);
                    List<Comment> comments = new ArrayList<>();
                    comments.add(new Comment(null, "test"));
                    book1.setComments(comments);
                    return book1;
                })
                .flatMap(bookRepository::save)
                .block();

        bookRepository.deleteById(book.getId()).block();

        Mono<Book> books = mongoTemplate.findById(book.getId(), Book.class, "books");
        StepVerifier
                .create(books)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}