package ru.tikskit.repository;

import ru.tikskit.domain.Comment;

import java.util.Optional;

public interface DBCommentRepository {
    Optional<Comment> getComment(long id);

    void addComment4Book(String text, long bookId);

    Comment changeComment(Comment comment);

    void deleteComment(Comment comment);
}
