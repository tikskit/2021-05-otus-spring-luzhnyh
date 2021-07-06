package ru.tikskit.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void insert(Book book) {
        SqlParameterSource paramSource = new MapSqlParameterSource(
                Map.of(
                        "name", book.getName(),
                        "genre_id", book.getGenreId(),
                        "author_id", book.getAuthorId()
                )
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into books(name, genre_id, author_id) values(:name, :genre_id, :author_id)",
                paramSource, keyHolder);
        long id = (Long) keyHolder.getKey();
        book.setId(id);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select id, name, genre_id, author_id from books", new BookRowMapper());
    }

    @Override
    public Book getById(long id) {
        return jdbc.queryForObject("select id, name, genre_id, author_id from books where id = :id",
                Map.of("id", id), new BookRowMapper());
    }

    @Override
    public void update(Book book) {
        jdbc.update("update books set name = :name, genre_id = :genre_id, author_id = :author_id where id = :id",
                Map.of(
                        "name", book.getName(),
                        "genre_id", book.getGenreId(),
                        "author_id", book.getAuthorId(),
                        "id", book.getId()
                )
        );
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            long genreId = rs.getLong("genre_id");
            long authorId = rs.getLong("author_id");

            return new Book(id, name, genreId, authorId);
        }
    }
}
