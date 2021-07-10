package ru.tikskit.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Author;
import ru.tikskit.domain.Book;
import ru.tikskit.domain.BookFull;
import ru.tikskit.domain.Genre;

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
    public Book insert(Book book) {
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
        return new Book(id, book);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select id, name, genre_id, author_id from books", new BookRowMapper());
    }

    @Override
    public List<BookFull> getAllFull() {
        return jdbc.query("select b.id as id_b, b.name as name_b" +
                ", g.id as id_g, g.name as name_g" +
                ", a.id as id_a, a.surname as surname_a, a.name as name_a " +
                "from books as b " +
                "inner join genres as g on b.genre_id = g.id " +
                "inner join authors as a on b.author_id = a.id", new BookFullRowMapper());
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

    private static class BookFullRowMapper implements RowMapper<BookFull> {

        private Author getAuthor(ResultSet rs) throws SQLException {
            long id = rs.getLong("id_a");
            String surname = rs.getString("surname_a");
            String name = rs.getString("name_a");
            return new Author(id, surname, name);
        }

        private Genre getGenre(ResultSet rs) throws SQLException {
            long id = rs.getLong("id_g");
            String name = rs.getString("name_g");
            return new Genre(id, name);
        }

        private BookFull getBook(ResultSet rs, Author author, Genre genre) throws SQLException {
            long id = rs.getLong("id_b");
            String name = rs.getString("name_b");
            return new BookFull(id, name, author, genre);
        }

        @Override
        public BookFull mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = getAuthor(rs);
            Genre genre = getGenre(rs);
            return getBook(rs, author, genre);
        }
    }
}
