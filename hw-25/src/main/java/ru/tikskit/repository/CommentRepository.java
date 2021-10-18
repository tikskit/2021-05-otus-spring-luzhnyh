package ru.tikskit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
