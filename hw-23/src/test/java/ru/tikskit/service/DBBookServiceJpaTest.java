package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.tikskit.repository.BookRepository;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.BookBuilder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@DisplayName("Репозиторий для книг должен")
@DataJpaTest
@Import({DBBookServiceJpa.class})
class DBBookServiceJpaTest {
    @Autowired
    DBBookService dbBookService;
    @MockBean
    BookRepository bookRepository;

    @DisplayName("добавлять одну и только одну книгу")
    @Test
    public void addBookShouldAddOnlyOneBook(){
        BookBuilder bookBuilder = new BookBuilder().
                setBookName("Американские боги").
                setAuthorId(30).
                setAuthorSurname("Гейман").
                setAuthorName("Нил").
                setGenreId(100).
                setGenreName("fantasy");
        Book newBook = bookBuilder.build();
        Book persistBook = bookBuilder.setBookId(50).build();

        when(bookRepository.save(newBook)).thenReturn(persistBook);
        dbBookService.addBook(newBook);
        verify(bookRepository, times(1)).save(newBook);
    }

    @DisplayName("правильно возвращать книгу по идентификатору")
    @Test
    public void getBookShouldReturnProperEntity() {
        when(bookRepository.getById(1050L)).thenReturn(
                new BookBuilder().
                        setBookId(1050L).
                        setBookName("Черная эстафета").
                        setAuthorId(31).
                        setAuthorSurname("Васильев").
                        setAuthorName("Владимир").
                        setGenreId(100).
                        setGenreName("fantasy").
                        build()

        );
        dbBookService.getBook(1050L);
        verify(bookRepository, times(1)).getById(1050L);
    }

    @DisplayName("правильно выбирать все книги из таблицы books")
    @Test
    public void getAllShouldReturnAllBooks() {
        dbBookService.getAll();
        verify(bookRepository, times(1)).findAll();
    }

    @DisplayName("правильно удалять книги из БД")
    @Test
    public void entityDisappearsWhenItDeleted() {
        Book deletedBook = new BookBuilder().
                setBookId(1050L).
                setBookName("Черная эстафета").
                setAuthorId(31).
                setAuthorSurname("Васильев").
                setAuthorName("Владимир").
                setGenreId(100).
                setGenreName("fantasy").
                build();
        dbBookService.deleteBook(deletedBook);
        verify(bookRepository, times(1)).delete(deletedBook);
    }

    @DisplayName("правильно удалять книги из БД по идентификатору")
    @Test
    public void entityDisappearsWhenItDeletedById() {
        dbBookService.deleteBookById(1050L);
        verify(bookRepository, times(1)).deleteById(1050L);
    }

    @DisplayName("правильно изменять книги в БД")
    @Test
    public void bookChangedProperly() {
        Book changedBook = new BookBuilder().
                setBookId(1050L).
                setBookName("Черная эстафета").
                setAuthorId(31).
                setAuthorSurname("Васильев").
                setAuthorName("Владимир").
                setGenreId(100).
                setGenreName("fantasy").
                build();

        dbBookService.changeBook(changedBook);
        verify(bookRepository, times(1)).save(changedBook);
    }

}