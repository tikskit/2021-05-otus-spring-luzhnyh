package ru.tikskit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
