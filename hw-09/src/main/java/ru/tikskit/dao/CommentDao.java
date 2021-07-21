package ru.tikskit.dao;

import ru.tikskit.domain.Comment;

public interface CommentDao {

    Comment getById(long id);

    Comment update(Comment comment);

    void delete(Comment comment);

}
