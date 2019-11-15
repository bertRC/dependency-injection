package ru.itpark.implementation.repository;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.model.Auto;
import ru.itpark.implementation.util.JdbcTemplate;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;

@Component
public class AutoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource ds;

    public AutoRepository(JdbcTemplate jdbcTemplate) throws NamingException {
        this.jdbcTemplate = jdbcTemplate;
        InitialContext context = new InitialContext();
        ds = (DataSource) context.lookup(ResourcesPaths.dbPath);
        String sql = "CREATE TABLE IF NOT EXISTS autos (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, description TEXT NOT NULL, image TEXT);";
        jdbcTemplate.executeUpdate(ds, sql);
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
}
