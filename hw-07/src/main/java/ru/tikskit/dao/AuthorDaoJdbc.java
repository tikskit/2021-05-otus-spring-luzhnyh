package ru.tikskit.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.tikskit.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void insert(Author author) {
        SqlParameterSource paramSource = new MapSqlParameterSource(
                Map.of("surname", author.getSurname(), "name", author.getName()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into authors(surname, name) values(:surname, :name)", paramSource, keyHolder);
        long id = (Long) keyHolder.getKey();
        author.setId(id);
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, surname, name from authors", new AuthorRowMapper());
    }

    @Override
    public Author getById(long id) {
        return jdbc.queryForObject("select id, surname, name from authors where id=:id", Map.of("id", id),
                new AuthorRowMapper());
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String surname = rs.getString("surname");
            String name = rs.getString("name");
            return new Author(id, surname, name);
        }
    }
}
