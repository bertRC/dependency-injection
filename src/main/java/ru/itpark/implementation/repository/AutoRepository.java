package ru.itpark.implementation.repository;

import ru.itpark.exceptions.InitializationException;
import ru.itpark.exceptions.NotFoundException;
import ru.itpark.framework.annotation.Component;
import ru.itpark.domain.Auto;
import ru.itpark.implementation.util.JdbcTemplate;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class AutoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource ds;

    public AutoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            InitialContext context = new InitialContext();
            ds = (DataSource) context.lookup(ResourcesPaths.dbPath);
            String sql = "CREATE TABLE IF NOT EXISTS autos (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, description TEXT NOT NULL, image TEXT);";
            jdbcTemplate.executeUpdate(ds, sql);
        } catch (NamingException e) {
            throw new InitializationException(e);
        }
    }

    public List<Auto> getAll() {
        String sql = "SELECT id, name, description, image FROM autos;";
        return jdbcTemplate.executeQuery(ds, sql, rs ->
                new Auto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image")
                )
        );
    }

    public Optional<Auto> getById(long id) {
        String sql = "SELECT id, name, description, image FROM autos WHERE id = ?;";
        return jdbcTemplate.executeQueryForObject(ds, sql, stmt -> {
                    stmt.setLong(1, id);
                    return stmt;
                }, rs ->
                        new Auto(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("image")
                        )
        );
    }

    public Auto create(Auto auto) {
        String sql = "INSERT INTO autos (name, description, image) VALUES (?, ?, ?);";
        long id = jdbcTemplate.executeUpdateWithGeneratedKey(ds, sql, stmt -> {
            stmt.setString(1, auto.getName());
            stmt.setString(2, auto.getDescription());
            stmt.setString(3, auto.getImage());
            return stmt;
        });
        auto.setId(id);
        return auto;
    }

    public Optional<String> getImage(long id) {
        String sql = "SELECT image FROM autos WHERE id = ?;";
        return jdbcTemplate.executeQueryForObject(ds, sql, stmt -> {
            stmt.setLong(1, id);
            return stmt;
        }, rs -> rs.getString("image"));
    }

    public String removeById(long id) {
        String image = getImage(id).orElseThrow(() -> new NotFoundException(String.format("Object with id %d not found", id)));
        String sql = "DELETE FROM autos WHERE id = ?;";
        jdbcTemplate.executeUpdate(ds, sql, stmt -> {
            stmt.setLong(1, id);
            return stmt;
        });
        return image;
    }

    public List<Auto> search(String text) {
        String sql = "SELECT id, name, description, image FROM autos WHERE name LIKE ? OR description LIKE ?;";
        return jdbcTemplate.executeQuery(ds, sql, stmt -> {
            stmt.setString(1, "%" + text + "%");
            stmt.setString(2, "%" + text + "%");
            return stmt;
        }, rs ->
                new Auto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image")
                ));
    }
}
