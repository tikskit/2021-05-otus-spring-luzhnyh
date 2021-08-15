package ru.tikskit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tikskit.domain.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
