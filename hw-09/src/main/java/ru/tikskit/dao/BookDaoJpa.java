package ru.tikskit.dao;

import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private final EntityManager em;

    public BookDaoJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Book insert(Book book) {
        Author author = em.merge(Objects.requireNonNull(book.getAuthor(), "Автор не задан"));
        Genre genre = em.merge(Objects.requireNonNull(book.getGenre(), "Жанр не задан"));

        Book newBook = new Book(book.getId(), book.getName(), author, genre, book.getComments());

        em.persist(newBook);
        return newBook;
    }

    @Override
    @Transactional
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b join fetch b.genre g join fetch b.author a",
                Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Book getById(long id) {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b join fetch b.genre g join fetch b.author a where b.id = :id",
                Book.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public Book update(Book book) {
        return em.merge(book);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        em.remove(book);
    }
}
