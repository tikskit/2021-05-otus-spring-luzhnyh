package ru.tikskit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Author;

public interface AuthorDao extends JpaRepository<Author, Long> {

}
