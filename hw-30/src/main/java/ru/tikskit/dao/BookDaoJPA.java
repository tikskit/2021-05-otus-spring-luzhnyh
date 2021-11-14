package ru.tikskit.dao;

import ru.tikskit.domain.Book;

import java.util.List;

public class BookDaoJPA implements BookDaoHealthCare {
    @Override
    public List<Book> findDoubles() {
        return null;
    }
}
