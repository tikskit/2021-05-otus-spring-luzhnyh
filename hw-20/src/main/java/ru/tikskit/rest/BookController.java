package ru.tikskit.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.GenreRepository;

import java.util.ArrayList;


@AllArgsConstructor
@RestController
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @GetMapping("/api/book")
    public Flux<Book> listPage() {
        return bookRepository.findAll();
    }

    @PostMapping("/api/book")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Book> createBook(@RequestParam("name") String bookName, @RequestParam("genreid") String genreId,
                              @RequestParam("authorid") String authorId) {

        return genreRepository.findById(genreId).
                zipWith(authorRepository.findById(authorId)).
                map(t -> new Book(null, bookName, t.getT2(), t.getT1(), null)).
                flatMap(bookRepository::save);
    }

    @PatchMapping("/api/book/{bookid}")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<Book> updateBook(@PathVariable("bookid") String bookId, @RequestParam("name") String bookName,
                              @RequestParam("genreid") String genreId, @RequestParam("authorid") String authorId) {

        return genreRepository.findById(genreId).
                zipWith(authorRepository.findById(authorId)).
                zipWith(bookRepository.findById(bookId)).
                map(objects -> {
                    objects.getT2().setName(bookName);
                    objects.getT2().setAuthor(objects.getT1().getT2());
                    objects.getT2().setGenre(objects.getT1().getT1());
                    return objects.getT2();
                }).
                flatMap(bookRepository::save);
    }

    @DeleteMapping("/api/book/{bookid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable("bookid") String bookId) {
        return bookRepository.deleteById(bookId);
    }

    @PutMapping("/api/book/{bookid}/comment")
    public Mono<Book> postComment(@PathVariable("bookid") String bookId, @RequestParam("text") String text) {
        return bookRepository.
                findById(bookId).
                map(b -> {
                    Comment comment = new Comment();
                    comment.setText(text);
                    if (b.getComments() == null) {
                        b.setComments(new ArrayList<>());
                    }
                    b.getComments().add(comment);
                    return b;
                }).
                flatMap(bookRepository::save);
    }
}
