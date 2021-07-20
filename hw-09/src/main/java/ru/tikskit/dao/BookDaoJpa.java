package ru.tikskit.dao;

import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

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
        em.persist(book);
        return book;
    }

    @Override
    @Transactional
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.genre g join fetch b.author a", Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Book getById(long id) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.genre g join fetch b.author a where b.id = :id", Book.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public void update(Book book) {
        em.merge(book);
    }

    @Override
    @Transactional
    public void deleteById(Book book) {
        em.remove(book);
    }
}
