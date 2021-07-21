package ru.tikskit.dao;

import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class CommentDaoJpa implements CommentDao {

    @PersistenceContext
    private final EntityManager em;

    public CommentDaoJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Comment getById(long id) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where id =: id", Comment.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {
        return em.merge(comment);
    }

    @Override
    @Transactional
    public void delete(Comment comment) {
        em.remove(comment);
    }
}
