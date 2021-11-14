package ru.tikskit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tikskit.domain.Book;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Long> {

/*
    @Query("")
    List<Book> findDoubles();
*/

}
