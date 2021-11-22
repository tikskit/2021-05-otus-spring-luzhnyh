package ru.tikskit.service;

import ru.tikskit.domain.Comment;

import java.util.Optional;

public interface DBCommentService {
    Optional<Comment> getComment(long id);

    void addComment4Book(String text, long bookId);

    Comment changeComment(Comment comment);

    void deleteComment(Comment comment);
}
