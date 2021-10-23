package ru.tikskit.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureTestDatabase
@DisplayName("Контроллер страниц должен")
class BookPagesControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DBBookService bookService;

    @DisplayName("пускать на индексную страницу неавторизованных")
    @Test
    public void shouldPassOnIndexPageWhenAnonymous() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "God",
            authorities = {"BOOK_MANAGER", "BOOK_SUPPORTER", "BOOK_REVIEWER"}
    )
    @DisplayName("пускать на индексную страницу имеющего любую роль")
    @Test
    public void shouldPassOnIndexPageWithAnyRole() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER"}
    )
    @DisplayName("пускать на страницу добавления книги ROLE_BOOK_MANAGER")
    @Test
    public void shouldPassOnaddbookPageWhenAuthorized() throws Exception {
        mvc.perform(get("/addbook"))
                .andExpect(status().isOk());
    }

    @DisplayName("перенаправлять неавторизованного пользователя на страницу авторизации")
    @Test
    public void shouldntRedirectaddbookPageWhenNotAuthorized() throws Exception {
        mvc.perform(get("/addbook"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "Supporter",
            authorities = {"ROLE_BOOK_SUPPORTER", "ROLE_BOOK_REVIEWER"}
    )
    @DisplayName("запрещать доступ на страницу /addbook всем, кто не ROLE_BOOK_MANAGER")
    @Test
    public void shouldForbidOnaddbookPageWhenNotManager() throws Exception {
        mvc.perform(get("/addbook"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "Supporter",
            authorities = {"ROLE_BOOK_SUPPORTER"}
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
    public void shouldRedirecteditbookPageWhenAnonymous() throws Exception {
        mvc.perform(get("/editbook").param("id", Long.toString(10L)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @WithMockUser(
            username = "Manager",
            authorities = {"ROLE_BOOK_MANAGER", "ROLE_BOOK_REVIEWER"}
    )
    @DisplayName("запрещать доступ пользователю, если он не ROLE_BOOK_SUPPORTER")
    @Test
    public void shouldForbideditbookPageWhenNotSupporter() throws Exception {
        mvc.perform(get("/editbook").param("id", Long.toString(10L)))
                .andExpect(status().isForbidden());
    }
}