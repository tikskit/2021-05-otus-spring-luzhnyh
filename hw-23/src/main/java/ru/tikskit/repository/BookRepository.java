package ru.tikskit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
