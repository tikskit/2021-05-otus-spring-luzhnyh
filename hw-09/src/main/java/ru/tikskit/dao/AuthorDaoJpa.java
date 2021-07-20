package ru.tikskit.dao;

import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AuthorDaoJpa implements AuthorDao {

    @PersistenceContext
    private final EntityManager em;

    public AuthorDaoJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Author insert(Author author) {
        em.persist(author);
        return author;
    }

    @Override
    @Transactional
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    @Transactional
    public Author getById(long id) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.id = :id", Author.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
