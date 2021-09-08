package ru.tikskit.service;

import ru.tikskit.domain.Book;

import java.util.Optional;

public interface CommentService {
    Optional<Book> addComment(String bookName, String authorSurname, String authorName, String commentText);
    Optional<Book> changeComment(String bookName, String authorSurname, String authorName, String newCommentText,
                                 String commentStartsWith, String commentEndsWith) throws AmbiguousCommentsFound, NoMatchingComment;
    Optional<Book> deleteComment(String bookName, String authorSurname, String authorName, String commentStartsWith,
                       String commentEndsWith) throws AmbiguousCommentsFound, NoMatchingComment;
}
