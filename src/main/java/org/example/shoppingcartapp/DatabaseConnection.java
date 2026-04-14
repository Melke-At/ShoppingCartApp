package org.example.shoppingcartapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/shopping_cart_localization";

    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASS");

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}