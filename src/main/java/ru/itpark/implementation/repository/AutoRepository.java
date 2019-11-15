package ru.itpark.implementation.repository;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.util.JdbcTemplate;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Component
public class AutoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource ds;

    public AutoRepository(JdbcTemplate jdbcTemplate) throws NamingException {
        this.jdbcTemplate = jdbcTemplate;
        InitialContext context = new InitialContext();
        ds = (DataSource) context.lookup(ResourcesPaths.dbPath);
    }
}
