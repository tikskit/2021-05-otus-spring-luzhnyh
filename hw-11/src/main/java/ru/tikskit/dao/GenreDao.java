package ru.tikskit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Genre;

public interface GenreDao extends JpaRepository<Genre, Long> {
}
