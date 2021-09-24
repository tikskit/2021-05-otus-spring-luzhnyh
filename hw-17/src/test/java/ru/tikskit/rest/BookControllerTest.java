package ru.tikskit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.tikskit.dao.AuthorDao;
import ru.tikskit.dao.BookDao;
import ru.tikskit.dao.GenreDao;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.rest.dto.AuthorConverter;
import ru.tikskit.rest.dto.BookConverter;
import ru.tikskit.rest.dto.BookDto;
import ru.tikskit.rest.dto.GenreConverter;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("REST контроллер книг")
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private GenreDao genreDao;
    @Autowired
    private BookConverter bookConverter;
    @Autowired
    private AuthorConverter authorConverter;
    @Autowired
    private GenreConverter genreConverter;
    @Autowired
    private EntityManager em;

    private Author vasilyev;
    private Genre fantasy;
    private Author lukyanenko;
    private Genre skiFi;

    @BeforeEach
    public void setUp() {
        vasilyev = authorDao.save(new Author(0, "Васильев", "Владимир"));
        fantasy = genreDao.save(new Genre(0, "fantasy"));
        lukyanenko = authorDao.save(new Author(0, "Лукьяненко", "Сергей"));
        skiFi = genreDao.save(new Genre(0, "sci-fi"));
    }

    @DisplayName("Должен возвращать все книги")
    @Test
    @Transactional
    public void shouldReturnAllTheBooks() throws Exception {
        Book theDeath = bookDao.save(new Book(0, "The death", vasilyev, fantasy, null));
        Book theDarkness = bookDao.save(new Book(0, "The darkness", vasilyev, fantasy, null));
        List<BookDto> booksDtoList = List.of(theDeath, theDarkness).stream().map(bookConverter::toDto).
                collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        String booksJson = mapper.writeValueAsString(booksDtoList);

        mvc.perform(get("/api/book")).
                andExpect(status().isOk()).
                andExpect(content().json(booksJson));
    }

    @DisplayName("Должен возвращать в JSON обновленную книгу")
    @Transactional
    @Test
    public void updateShouldReturnUpdatedBook() throws Exception {
        Book theDeath = bookDao.save(new Book(0, "The death", vasilyev, fantasy, null));

        BookDto bookDto = bookConverter.toDto(theDeath);
        bookDto.setName("the darkness");
        bookDto.setAuthor(authorConverter.toDto(lukyanenko));
        bookDto.setGenre(genreConverter.toDto(skiFi));

        ObjectMapper mapper = new ObjectMapper();
        String bookJson = mapper.writeValueAsString(bookDto);

        mvc.perform(
                patch("/api/book/" + theDeath.getId()).
                        param("name", bookDto.getName()).
                        param("genreid", Long.toString(bookDto.getGenre().getId())).
                        param("authorid", Long.toString(bookDto.getAuthor().getId()))
        ).
                andExpect(status().is2xxSuccessful()).
                andExpect(content().json(bookJson));
    }

    @DisplayName("Должен обновлять книги в БД")
    @Transactional
    @Test
    public void updateShouldUpdateBookInDB() throws Exception {
        Book theDeath = bookDao.save(new Book(0, "The death", vasilyev, fantasy, null));

        BookDto bookDto = bookConverter.toDto(theDeath);
        bookDto.setName("the darkness");
        bookDto.setAuthor(authorConverter.toDto(lukyanenko));
        bookDto.setGenre(genreConverter.toDto(skiFi));

        mvc.perform(
                patch("/api/book/" + theDeath.getId()).
                        param("name", bookDto.getName()).
                        param("genreid", Long.toString(bookDto.getGenre().getId())).
                        param("authorid", Long.toString(bookDto.getAuthor().getId()))
        ).
                andExpect(status().is2xxSuccessful());

        Book actualBook = bookDao.getById(bookDto.getId());
        Book expected = bookConverter.toBook(bookDto);

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Должен возвращать в JSON созданную книгу")
    @Test
    public void shouldReturnJSONWithNewBook() throws Exception {
        BookDto bookDto = new BookDto(0, "The death", authorConverter.toDto(vasilyev),
                genreConverter.toDto(fantasy));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.valueToTree(bookDto);
        jsonNode.remove("id"); // Удалим из JSON идентификатор новой книги, чтобы далее сравнивать только все остальные значения

        mvc.perform(
                post("/api/book").
                        param("name", bookDto.getName()).
                        param("genreid", Long.toString(bookDto.getGenre().getId())).
                        param("authorid", Long.toString(bookDto.getAuthor().getId()))
        ).
                andExpect(status().is2xxSuccessful()).
        andExpect(content().json(jsonNode.toString()));
    }

    @DisplayName("Вставлять созданную книгу в таблицу")
    @Test
    @Transactional(readOnly = true)
    public void shouldInsertNewBookInDB() throws Exception {
        MvcResult mvcResult = mvc.perform(
                post("/api/book").
                        param("name", "The death").
                        param("genreid", Long.toString(fantasy.getId())).
                        param("authorid", Long.toString(vasilyev.getId()))
        ).
                andExpect(status().is2xxSuccessful()).andReturn();

        String content = mvcResult.getResponse().getContentAsString(UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        BookDto expectedDto = mapper.readValue(content, BookDto.class);

        Book actual = bookDao.getById(expectedDto.getId());
        BookDto actualDto = bookConverter.toDto(actual);
        assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @DisplayName("Удалять книгу из БД")
    @Test
    @Transactional(readOnly = true)
    public void shouldDeleteBookFromDB() throws Exception {
        Book theDeath = bookDao.save(new Book(0, "The death", vasilyev, fantasy, null));
        long bookId = theDeath.getId();
        mvc.perform(
                delete("/api/book/" + bookId)
        ).andExpect(status().is2xxSuccessful());

        em.flush();
        assertThat(bookDao.existsById(bookId)).isFalse();
    }

}
