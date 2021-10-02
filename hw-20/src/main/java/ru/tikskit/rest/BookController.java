package ru.tikskit.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.controller.AuthorNotFoundException;
import ru.tikskit.controller.BookCrudException;
import ru.tikskit.controller.GenreNotFoundException;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.domain.Genre;
import ru.tikskit.rest.dto.BookConverter;
import ru.tikskit.rest.dto.BookDto;
import ru.tikskit.rest.dto.CommentConverter;
import ru.tikskit.rest.dto.CommentDto;
import ru.tikskit.service.DBAuthorService;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.DBGenreService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class BookController {
    private final DBBookService bookService;
    private final DBAuthorService authorService;
    private final DBGenreService genreService;
    private final CommentConverter commentConverter;
    private final BookConverter bookConverter;

    @GetMapping("/api/book")
    public List<BookDto> listPage() {
        return bookService.getAll().stream().map(bookConverter::toDto).collect(Collectors.toList());
    }

    @PostMapping("/api/book")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BookDto createBook(@RequestParam("name") String name, @RequestParam("genreid") long genreId,
                              @RequestParam("authorid") long authorId) {

        Author author = authorService.getAuthor(authorId).orElseThrow(AuthorNotFoundException::new);
        Genre genre = genreService.getGenre(genreId).orElseThrow(GenreNotFoundException::new);

        Book book = bookService.addBook(new Book(0, name, author, genre, null));
        return bookConverter.toDto(book);
    }

    @PatchMapping("/api/book/{bookid}")
    @ResponseStatus(code = HttpStatus.OK)
    public BookDto updateBook(@PathVariable("bookid") long bookId, @RequestParam("name") String name,
                              @RequestParam("genreid") long genreId, @RequestParam("authorid") long authorId) {

        Book book = bookService.getBook(bookId).orElseThrow(BookCrudException::new);
        Author author = authorService.getAuthor(authorId).orElseThrow(AuthorNotFoundException::new);
        Genre genre = genreService.getGenre(genreId).orElseThrow(GenreNotFoundException::new);
        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);
        bookService.changeBook(book);
        return bookConverter.toDto(book);
    }

    @DeleteMapping("/api/book/{bookid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("bookid") long bookId) {
        bookService.deleteBookById(bookId);
    }

    @PostMapping("/api/book/{bookid}/comment")
    public CommentDto postComment(@PathVariable("bookid") long bookId, @RequestParam("text") String text) {
        Book book = bookService.getBook(bookId).orElseThrow(BookCrudException::new);
        Comment comment = new Comment();
        comment.setText(text);
        book.getComments().add(comment);
        bookService.changeBook(book);
        return commentConverter.toDto(comment);
    }
}
