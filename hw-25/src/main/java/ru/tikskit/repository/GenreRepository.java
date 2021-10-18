package ru.tikskit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
