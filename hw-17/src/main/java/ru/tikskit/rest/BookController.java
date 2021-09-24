package ru.tikskit.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.dao.BookDao;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.domain.Genre;
import ru.tikskit.rest.dto.BookConverter;
import ru.tikskit.rest.dto.BookDto;
import ru.tikskit.rest.dto.CommentConverter;
import ru.tikskit.rest.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CommentConverter commentConverter;
    private final BookConverter bookConverter;

    public BookController(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, CommentConverter commentConverter,
                          BookConverter bookConverter) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.commentConverter = commentConverter;
        this.bookConverter = bookConverter;
    }

    @GetMapping("/api/book")
    public List<BookDto> listPage() {
        return bookDao.findAll().stream().map(bookConverter::toDto).collect(Collectors.toList());
    }

    @PostMapping("/api/book")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BookDto createBook(@RequestParam("name") String name, @RequestParam("genreid") long genreId,
                              @RequestParam("authorid") long authorId) {

        Author author = authorDao.getById(authorId);
        Genre genre = genreDao.getById(genreId);

        Book book = bookDao.save(new Book(0, name, author, genre, null));
        return bookConverter.toDto(book);
    }

    @PatchMapping("/api/book/{bookid}")
    @ResponseStatus(code = HttpStatus.OK)
    public BookDto updateBook(@PathVariable("bookid") long bookId, @RequestParam("name") String name,
                              @RequestParam("genreid") long genreId, @RequestParam("authorid") long authorId) {

        Book book = bookDao.getById(bookId);
        Author author = authorDao.getById(authorId);
        Genre genre = genreDao.getById(genreId);
        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);
        bookDao.save(book);
        return bookConverter.toDto(book);
    }

    @DeleteMapping("/api/book/{bookid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("bookid") long bookId) {
        List<Book> allById = bookDao.findAllById(List.of(bookId));
        bookDao.deleteById(bookId);
    }

    @PostMapping("/api/book/{bookid}/comment")
    public CommentDto postComment(@PathVariable("bookid") long bookId, @RequestParam("text") String text) {
        Book book = bookDao.getById(bookId);
        Comment comment = new Comment();
        comment.setText(text);
        book.getComments().add(comment);
        bookDao.save(book);
        return commentConverter.toDto(comment);
    }
}
