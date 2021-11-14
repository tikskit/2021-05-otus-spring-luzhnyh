package ru.tikskit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tikskit.domain.Book;
import ru.tikskit.service.DBAuthorService;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.DBGenreService;

@Controller
public class BookPagesController {

    private final DBBookService bookService;
    private final DBGenreService genreService;
    private final DBAuthorService authorService;

    public BookPagesController(DBBookService bookService, DBGenreService genreService, DBAuthorService authorService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String listPage() {
        return "books";
    }

    @GetMapping("/addbook")
    public String addBookPage(Model model) {
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        return "addbook";
    }

    @GetMapping("/editbook")
    public String editBook(@RequestParam("id") Book book, Model model) {
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("comments", book.getComments());
        return "editbook";
    }

}
