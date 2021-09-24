package ru.tikskit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.dao.BookDao;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Book;

@Controller
public class BookPagesController {

    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;

    public BookPagesController(BookDao bookDao, GenreDao genreDao, AuthorDao authorDao) {
        this.bookDao = bookDao;
        this.genreDao = genreDao;
        this.authorDao = authorDao;
    }

    @GetMapping("/")
    public String listPage() {
        return "books";
    }

    @GetMapping("/addbook")
    public String addBookPage(Model model) {
        model.addAttribute("authors", authorDao.findAll());
        model.addAttribute("genres", genreDao.findAll());
        return "addbook";
    }

    @GetMapping("/editbook")
    public String editBook(@RequestParam("id") long id, Model model) {
        Book book = bookDao.getById(id);

        model.addAttribute("book", book);
        model.addAttribute("authors", authorDao.findAll());
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("comments", book.getComments());
        return "editbook";
    }

}
