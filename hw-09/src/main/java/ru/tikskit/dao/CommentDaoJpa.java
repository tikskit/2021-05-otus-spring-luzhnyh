package ru.tikskit.dao;

import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class CommentDaoJpa implements CommentDao {

    @PersistenceContext
    private final EntityManager em;

    public CommentDaoJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Comment getById(long id) {
        return em.find(Comment.class, id);
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
