package ru.tikskit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Comment;

public interface CommentDao extends JpaRepository<Comment, Long> {

}
