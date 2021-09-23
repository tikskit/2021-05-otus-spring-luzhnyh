package ru.tikskit.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.controller.BookCrudException;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Comment;
import ru.tikskit.rest.dto.CommentConverter;
import ru.tikskit.rest.dto.CommentDto;
import ru.tikskit.service.DBBookService;

@RestController
public class BookController {
    private final DBBookService bookService;
    private final CommentConverter commentConverter;

    public BookController(DBBookService bookService, CommentConverter commentConverter) {
        this.bookService = bookService;
        this.commentConverter = commentConverter;
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
