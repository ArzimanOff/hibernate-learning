package org.arzimanoff.hibernate.starter;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.arzimanoff.hibernate.entity.Birthday;
import org.arzimanoff.hibernate.entity.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HibernateRunnerTest {
    @Test
    public void testHibernateApi() throws SQLException {
        var user = User.builder()
                .username("arziman_off")
                .firstname("AAA")
                .lastname("Arz")
                .birthDate(new Birthday(LocalDate.of(2005, 5, 19)))
                .build();
        var sql = """
                insert into
                %s (%s)
                values
                    (%s)
                """;

        var tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(Table::name)
                .orElse(user.getClass().getName());

        var fields = user.getClass().getDeclaredFields();
        var columnNames = Arrays.stream(fields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName())
                ).collect(Collectors.joining(", "));

        var columnValues = Arrays.stream(fields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        try (var connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/hibernate-starter-db",
                "postgres",
                "12345678");
             var preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        ) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                preparedStatement.setObject(i + 1, fields[i].get(user));
            }
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}