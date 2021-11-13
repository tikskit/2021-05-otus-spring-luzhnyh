package ru.tikskit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.Book;

public interface BookDao extends JpaRepository<Book, Long> {

}
