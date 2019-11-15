package ru.itpark.implementation.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {
    PreparedStatement setValues(PreparedStatement stmt) throws SQLException;
}
