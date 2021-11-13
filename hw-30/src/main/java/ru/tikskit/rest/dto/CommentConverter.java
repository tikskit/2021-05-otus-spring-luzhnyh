package ru.tikskit.rest.dto;

import org.springframework.stereotype.Component;
import ru.tikskit.domain.Comment;

@Component
public class CommentConverter {
    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
