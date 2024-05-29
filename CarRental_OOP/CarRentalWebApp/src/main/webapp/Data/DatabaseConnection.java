package main.webapp.Data;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/CarRental";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // пробуждаємо інстанс драйверу бдшки
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Ошибка при установке соединения с базой данных.", e);
        }
    }
}

