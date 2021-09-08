package ru.tikskit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;
import ru.tikskit.repository.AuthorRepository;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Сервис для книг должен")
@SpringBootTest
class BookServiceImplTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();

        genreRepository.save(new Genre(null, "fantasy"));
        authorRepository.save(new Author(null, "Лукьяненко", "Сергей"));
    }

    @DisplayName("Возвращать книгу, которая существует")
    @Test
    public void shouldFindBookReturnsBookThatExists() {
        Optional<Author> lukyanenko = authorRepository.findBySurnameAndName("Лукьяненко", "Сергей");
        Optional<Genre> fantasy = genreRepository.findByName("fantasy");
        Book death = new Book(null, "Смерть", lukyanenko.get(), fantasy.get(), null);
        Book expected = bookRepository.save(death);

        Optional<Book> actual = bookService.findBook("Смерть", "Лукьяненко", "Сергей");

        assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("выбрасывать BookServiceException, если искомая книга не существует")
    @Test
    public void shouldThrowBookServiceExceptionWhenAuthorDoesntExist() {
        assertThatThrownBy(() -> bookService.findBook("Смерть", "Купер", "Алан")).
                isInstanceOf(BookServiceException.class);
    }

    @DisplayName("возвращать empty, если книги не существует")
    @Test
    public void shouldReturnEmptyWhenBookDoesntExist() {
        Optional<Book> book = bookService.findBook("Тьма", "Лукьяненко", "Сергей");
        assertThat(book).isEmpty();
    }
}