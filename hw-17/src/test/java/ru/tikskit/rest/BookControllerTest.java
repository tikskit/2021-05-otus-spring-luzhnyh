package ru.tikskit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.BookBuilder;
import ru.tikskit.domain.Genre;
import ru.tikskit.rest.dto.BookConverter;
import ru.tikskit.rest.dto.BookDto;
import ru.tikskit.service.DBAuthorService;
import ru.tikskit.service.DBBookService;
import ru.tikskit.service.DBGenreService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("REST контроллер книг")
public class BookControllerTest {
    @MockBean
    private DBBookService bookService;
    @MockBean
    private DBAuthorService authorService;
    @MockBean
    private DBGenreService genreService;


    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookConverter bookConverter;

    @DisplayName("Должен возвращать все книги")
    @Test
    public void shouldReturnAllTheBooks() throws Exception {
        Author vasilyev = new Author(0, "Васильев", "Владимир");
        Genre fantasy = new Genre(0, "fantasy");

        when(bookService.getAll()).thenReturn(
                List.of(
                        new Book(0, "The death", vasilyev, fantasy, null),
                        new Book(0, "The darkness", vasilyev, fantasy, null)
                )
        );

        List<BookDto> booksDtoList = List.of(
                new Book(0, "The death", vasilyev, fantasy, null),
                new Book(0, "The darkness", vasilyev, fantasy, null)
        ).stream().map(bookConverter::toDto).collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        String booksJson = mapper.writeValueAsString(booksDtoList);

        mvc.perform(get("/api/book")).
                andExpect(status().isOk()).
                andExpect(content().json(booksJson));
    }

    @DisplayName("Должен возвращать в JSON обновленную книгу")
    @Test
    public void updateShouldReturnUpdatedBook() throws Exception {
        when(authorService.getAuthor(6)).thenReturn(Optional.of(new Author(6, "Лукьяненко", "Сергей")));
        when(genreService.getGenre(11)).thenReturn(Optional.of(new Genre(11, "sci-fi")));
        when(bookService.getBook(10)).thenReturn(Optional.of(
                new BookBuilder().
                        setBookId(10).
                        setBookName("The death").
                        setAuthorId(5).
                        setAuthorName("Владимир").
                        setAuthorSurname("Васильев").
                        setGenreId(12).
                        setGenreName("fantasy").build()));

        Book currentBook = new BookBuilder().
                setBookId(10).
                setBookName("The death").
                setAuthorId(5).
                setAuthorName("Владимир").
                setAuthorSurname("Васильев").
                setGenreId(12).
                setGenreName("fantasy").build();
        Book changedBook = new BookBuilder().
                setBookId(10).
                setBookName("the darkness").
                setAuthorId(6).
                setAuthorName("Сергей").
                setAuthorSurname("Лукьяненко").
                setGenreId(11).
                setGenreName("sci-fi").build();
        BookDto bookDto = bookConverter.toDto(changedBook);

        ObjectMapper mapper = new ObjectMapper();
        String bookJson = mapper.writeValueAsString(bookDto);

        mvc.perform(
                patch("/api/book/" + currentBook.getId()).
                        param("name", bookDto.getName()).
                        param("genreid", Long.toString(11)).
                        param("authorid", Long.toString(6))
        ).
                andExpect(status().is2xxSuccessful()).
                andExpect(content().json(bookJson));
    }

    @DisplayName("Должен обновлять книги в БД")
    @Test
    public void updateShouldUpdateBookInDB() throws Exception {
        when(bookService.getBook(101)).thenReturn(Optional.of(
                new BookBuilder().
                        setBookId(101).
                        setBookName("The death").
                        setAuthorId(2).
                        setAuthorSurname("Васильев").
                        setAuthorName("Владимир").
                        setGenreId(53).
                        setGenreName("fantasy").
                        build()
        ));
        when(authorService.getAuthor(3)).thenReturn(Optional.of(new Author(3, "Лукьяненко", "Сергей")));
        when(genreService.getGenre(54)).thenReturn(Optional.of(new Genre(54, "sci-fi")));
        Book changedBook = new BookBuilder().
                setBookId(101).
                setBookName("the darkness").
                setAuthorId(3).
                setAuthorSurname("Лукьяненко").
                setAuthorName("Сергей").
                setGenreId(54).
                setGenreName("sci-fi").
                build();

        mvc.perform(
                patch("/api/book/" + 101).
                        param("name", "the darkness").
                        param("genreid", Long.toString(54)).
                        param("authorid", Long.toString(3))
        ).
                andExpect(status().is2xxSuccessful());

        verify(bookService, times(1)).changeBook(changedBook);
    }

    @DisplayName("Должен возвращать в JSON созданную книгу")
    @Test
    public void shouldReturnJSONWithNewBook() throws Exception {
        when(authorService.getAuthor(2)).thenReturn(Optional.of(new Author(2, "Васильев", "Владимир")));
        when(genreService.getGenre(53)).thenReturn(Optional.of(new Genre(53, "fantasy")));

        BookBuilder bookBuilder = new BookBuilder().
                setBookName("The death").
                setAuthorId(2).
                setAuthorSurname("Васильев").
                setAuthorName("Владимир").
                setGenreId(53).
                setGenreName("fantasy");

        Book newBook = bookBuilder.build();
        Book savedBook = bookBuilder.setBookId(1).build();

        BookDto savedBookDto = bookConverter.toDto(savedBook);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.valueToTree(savedBookDto);

        when(bookService.addBook(newBook)).thenReturn(savedBook);

        mvc.perform(
                post("/api/book").
                        param("name", savedBookDto.getName()).
                        param("genreid", Long.toString(53)).
                        param("authorid", Long.toString(2))
        ).
                andExpect(status().is2xxSuccessful()).
        andExpect(content().json(jsonNode.toString()));
    }

    @DisplayName("Вставлять созданную книгу в таблицу")
    @Test
    public void shouldInsertNewBookInDB() throws Exception {
        when(authorService.getAuthor(2)).thenReturn(Optional.of(new Author(2, "Васильев", "Владимир")));
        when(genreService.getGenre(53)).thenReturn(Optional.of(new Genre(53, "fantasy")));


        mvc.perform(
                post("/api/book").
                        param("name", "The death").
                        param("genreid", Long.toString(53)).
                        param("authorid", Long.toString(2))
        ).
                andExpect(status().is2xxSuccessful()).andReturn();

        Book createdBook = new BookBuilder().
                setBookName("The death").
                setAuthorId(2).
                setAuthorSurname("Васильев").
                setAuthorName("Владимир").
                setGenreId(53).
                setGenreName("fantasy").
                build();
        verify(bookService, times(1)).addBook(createdBook);

    }
    
    @DisplayName("Удалять книгу из БД")
    @Test
    public void shouldDeleteBookFromDB() throws Exception {
        mvc.perform(
                delete("/api/book/" + 100500)
        ).andExpect(status().is2xxSuccessful());

        verify(bookService, times(1)).deleteBookById(100500);
    }
}
