package ru.itpark.implementation.util;

import ru.itpark.exceptions.DataStoreException;
import ru.itpark.exceptions.NoGeneratedKeysException;
import ru.itpark.framework.annotation.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcTemplate {
    private <T> T executeInternal(DataSource ds, String sql, PreparedStatementExecutor<T> executor) {
        try (
                Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            return executor.execute(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataStoreException(e);
        }
    }

    public <T> List<T> executeQuery(DataSource ds, String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        return executeInternal(ds, sql, stmt -> {
            try (ResultSet rs = setter.setValues(stmt).executeQuery();) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
                return result;
            }
        });
    }

    public <T> List<T> executeQuery(DataSource ds, String sql, RowMapper<T> mapper) {
        return executeQuery(ds, sql, stmt -> stmt, mapper);
    }

    public <T> Optional<T> executeQueryForObject(DataSource ds, String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        return executeInternal(ds, sql, stmt -> {
            try (ResultSet rs = setter.setValues(stmt).executeQuery();) {
                if (rs.next()) {
                    return Optional.of(mapper.map(rs));
                }
                return Optional.empty();
            }
        });
    }

    public int executeUpdate(DataSource ds, String sql, PreparedStatementSetter setter) {
        return executeInternal(ds, sql, stmt ->
                setter.setValues(stmt).executeUpdate());
    }

    public int executeUpdate(DataSource ds, String sql) {
        return executeUpdate(ds, sql, stmt -> stmt);
    }

    public long executeUpdateWithGeneratedKey(DataSource ds, String sql, PreparedStatementSetter setter) {
        try (
                Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            setter.setValues(stmt).executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                throw new NoGeneratedKeysException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataStoreException(e);
        }
    }
}
