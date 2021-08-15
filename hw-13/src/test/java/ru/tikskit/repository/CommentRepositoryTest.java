package ru.tikskit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tikskit.domain.Comment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для комментариев должен")
@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @DisplayName("сохранять комментарии")
    @Test
    public void shouldSaveCommnet() {
        Comment comment = repository.save(new Comment(null, "test comment"));
        assertThat(comment.getId()).isNotEmpty();
    }

    @DisplayName("загружать комментарии по идентификатору")
    @Test
    public void shouldLoadAuthorById() {
        Comment comment = repository.save(new Comment(null, "test comment"));
        Optional<Comment> loaded = repository.findById(comment.getId());

        assertThat(loaded).isNotEmpty().get().usingRecursiveComparison().isEqualTo(comment);
    }
}