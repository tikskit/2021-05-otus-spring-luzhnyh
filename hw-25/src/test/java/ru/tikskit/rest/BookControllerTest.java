package ru.tikskit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DisplayName("REST контроллер книг должен")
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

    @DisplayName("возвращать все книги даже для неавторизованного посетителя")
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

    @WithMockUser(
            username = "Supporter",
            authorities = {"ROLE_BOOK_SUPPORTER"}
    )
    @DisplayName("возвращать в JSON обновленную книгу")
    @Test
    public void updateShouldReturnUpdatedBookWhenSupporter() throws Exception {
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

    @DisplayName("перенаправлять неавторизованного пользователя с PATCH /api/book на страницу авторизации")
    @Test
    public void shouldRedirectAtPatchApiBookWhenAnonymous() throws Exception {

        mvc.perform(
                patch("/api/book/" + 10).
                        param("name", "the darkness").
                        param("genreid", Long.toString(11)).
                        param("authorid", Long.toString(6))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER", "ROLE_BOOK_REVIEWER"}
    )
    @DisplayName("запрещать доступ пользователю с любыми правами, кроме BOOK_SUPPORTER с PATCH /api/book на страницу авторизации")
    @Test
    public void shouldForbidAtPatchApiBookWhenNotSupporter() throws Exception {

        mvc.perform(
                patch("/api/book/" + 10).
                        param("name", "the darkness").
                        param("genreid", Long.toString(11)).
                        param("authorid", Long.toString(6))
        )
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "Supporter",
            authorities = {"ROLE_BOOK_SUPPORTER"}
    )
    @DisplayName("обновлять книги в БД")
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

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER"}
    )
    @DisplayName("возвращать в JSON созданную книгу,  если пользователь ROLE_BOOK_MANAGER")
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

    @WithMockUser(
            username = "Supporter",
            authorities = {"ROLE_BOOK_SUPPORTER", "ROLE_BOOK_REVIEWER"}
    )
    @DisplayName("запрещать доступ для пользователя с любыми правами, кроме ROLE_BOOK_MANAGER с POST /api/book")
    @Test
    public void shouldForbidAtPostApiBookWhenNotManager() throws Exception {
        mvc.perform(
                post("/api/book").
                        param("name", "The death").
                        param("genreid", Long.toString(53)).
                        param("authorid", Long.toString(2))
        )
                .andExpect(status().isForbidden());
    }

    @DisplayName("перенаправлять неавторизованного пользователя с POST /api/book на страницу авторизации")
    @Test
    public void shouldRedirectAtPostApiBookWhenNotAuthorized() throws Exception {
        mvc.perform(
                post("/api/book").
                        param("name", "The death").
                        param("genreid", Long.toString(53)).
                        param("authorid", Long.toString(2))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER"}
    )
    @DisplayName("вставлять созданную книгу в таблицу")
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

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER"}
    )
    @DisplayName("удалять книгу из БД")
    @Test
    public void shouldDeleteBookFromDB() throws Exception {
        mvc.perform(
                delete("/api/book/" + 100500)
        ).andExpect(status().is2xxSuccessful());

        verify(bookService, times(1)).deleteBookById(100500);
    }

    @DisplayName("перенаправлять неавторизованного пользователя с DELETE /api/book на страницу авторизации")
    @Test
    public void shouldRedirectAtDeleteApiBookWhenNotAuthorized() throws Exception {
        mvc.perform(
                delete("/api/book/" + 100500)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "Supporter",
            authorities = {"ROLE_BOOK_SUPPORTER", "ROLE_BOOK_REVIEWER"}
    )
    @DisplayName("перенаправлять неавторизованного пользователя с DELETE /api/book на страницу авторизации")
    @Test
    public void shouldForbidAtDeleteApiBookWhenNotManager() throws Exception {
        mvc.perform(
                delete("/api/book/" + 100500)
        )
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "Reviewer",
            authorities = {"ROLE_BOOK_REVIEWER"}
    )
    @DisplayName("добавлять комментарий к книге")
    @Test
    public void shouldCommentBook() throws Exception {
        when(bookService.getBook(10)).thenReturn(
                Optional.of(
                        new BookBuilder()
                                .setBookId(10)
                                .setBookName("The death")
                                .setAuthorId(2)
                                .setAuthorSurname("Васильев")
                                .setAuthorName("Владимир")
                                .setGenreId(53)
                                .setGenreName("fantasy")
                                .addComment(1, "comment 1")
                                .addComment(2, "comment 2")
                                .build()
                )
        );

        mvc.perform(
                post(String.format("/api/book/%s/comment", 10))
                .param("text", "any comment")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("добавлять перенаправлять неавторизованного посетителя на страницу аутентификации")
    @Test
    public void shouldRedirectAtLoginPageCommentBookWhenNotAuth() throws Exception {
        mvc.perform(
                post(String.format("/api/book/%s/comment", 10))
                .param("text", "any comment")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER", "ROLE_BOOK_SUPPORTER"}
    )
    @DisplayName("добавлять перенаправлять неавторизованного посетителя на страницу аутентификации")
    @Test
    public void shouldForbidCommentBookWhenNotAuth() throws Exception {
        mvc.perform(
                post(String.format("/api/book/%s/comment", 10))
                .param("text", "any comment")
        )
                .andExpect(status().isForbidden());
    }
}
