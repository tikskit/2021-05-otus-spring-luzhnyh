package ru.tikskit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.service.DBAuthorService;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.DBGenreService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final DBBookService bookService;
    private final DBGenreService genreService;
    private final DBAuthorService authorService;

    public BookController(DBBookService bookService, DBGenreService genreService, DBAuthorService authorService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> allBooks = bookService.getAll();
        model.addAttribute("books", allBooks);
        return "books";
    }

    @GetMapping("/addbook")
    public String addBookPage(Model model) {
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        return "addbook";
    }

    @PostMapping("/postbook")
    public String postBook(@RequestParam("bookname") String bookname, @RequestParam("authorid") long authorId,
                           @RequestParam("genreid") long genreId) {
        Author author = authorService.getAuthor(authorId).orElseThrow(AuthorNotFoundException::new);
        Genre genre = genreService.getGenre(genreId).orElseThrow(GenreNotFoundException::new);

        Book book = new Book(0, bookname, author, genre, null);
        bookService.addBook(book);
        return "books";
    }

    @GetMapping("/deletebooks")
    @Transactional
    public String deleteBooks(@RequestParam("bookid") List<Long> ids) {
        for (Long id : ids) {
            bookService.deleteBookById(id);
        }
        return "books";
    }

    @GetMapping("/editbook")
    public String editBook(@RequestParam("id") long id, Model model) {
        Book book = bookService.getBook(id).orElseThrow(BookNotFoundException::new);

        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        return "editbook";
    }

    @PostMapping("/patchbook")
    public String patchBook(@RequestParam("bookid") long bookid, @RequestParam("bookname") String bookname,
                            @RequestParam("authorid") long authorId, @RequestParam("genreid") long genreId) {
        Author author = authorService.getAuthor(authorId).orElseThrow(AuthorNotFoundException::new);
        Genre genre = genreService.getGenre(genreId).orElseThrow(GenreNotFoundException::new);
        Book book = bookService.getBook(bookid).orElseThrow(BookNotFoundException::new);
        book.setName(bookname);
        book.setAuthor(author);
        book.setGenre(genre);
        bookService.changeBook(book);
        return "books";
    }
}
