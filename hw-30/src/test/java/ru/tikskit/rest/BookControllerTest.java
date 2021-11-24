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
}
