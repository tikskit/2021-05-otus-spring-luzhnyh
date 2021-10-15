package ru.tikskit.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.tikskit.domain.BookBuilder;
import ru.tikskit.service.DBBookService;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest
@AutoConfigureTestDatabase
@DisplayName("Контроллер страниц должен")
class BookPagesControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DBBookService bookService;

    @WithMockUser(
            username = "admin",
            authorities = {"ADMIN"}
    )
    @DisplayName("пускать на индексную страницу авторизованного посетителя")
    @Test
    public void shouldPassOnIndexPageWhenAuthorized() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @DisplayName("перенаправлять неавторизованного пользователя на страницу авторизации")
    @Test
    public void shouldntPassOnIndexPageWhenNotAuthorized() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ADMIN"}
    )
    @DisplayName("пускать на страницу добавления книги авторизованного посетителя")
    @Test
    public void shouldPassOnaddbookPageWhenAuthorized() throws Exception {
        mvc.perform(get("/addbook"))
                .andExpect(status().isOk());
    }

    @DisplayName("перенаправлять неавторизованного пользователя на страницу авторизации")
    @Test
    public void shouldntPassOnaddbookPageWhenNotAuthorized() throws Exception {
        mvc.perform(get("/addbook"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ADMIN"}
    )
    @DisplayName("пускать на страницу редактирования книги авторизованного посетителя")
    @Test
    public void shouldPassOneditbookPageWhenAuthorized() throws Exception {
        when(bookService.getBook(10)).thenReturn(
                Optional.of(
                        new BookBuilder()
                        .setBookId(10)
                        .setBookName("Американские боги")
                        .setAuthorId(30)
                        .setAuthorSurname("Гейман")
                        .setAuthorName("Нил")
                        .setGenreId(100)
                        .setGenreName("fantasy")
                                .build()));
        mvc.perform(get("/editbook").param("id", Long.toString(10L)))
                .andExpect(status().isOk());
    }

    @DisplayName("перенаправлять неавторизованного пользователя на страницу авторизации")
    @Test
    public void shouldntPassOneditbookPageWhenNotAuthorized() throws Exception {
        mvc.perform(get("/editbook").param("id", Long.toString(10L)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }
}