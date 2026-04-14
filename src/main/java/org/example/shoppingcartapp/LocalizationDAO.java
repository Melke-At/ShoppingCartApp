package org.example.shoppingcartapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationDAO {

    private final Connection conn;

    public LocalizationDAO() {
        try {
            this.conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to database", e);
        }
    }

    /**
     * Loads all localized UI strings for a given language code.
     *
     * @param languageCode e.g. "en", "fi", "sv", "ja", "ar"
     * @return Map of key → translated value
     */
    public Map<String, String> loadLanguage(String languageCode) {
        Map<String, String> translations = new HashMap<>();

        String sql = """
            SELECT `key`, value 
            FROM localization_strings 
            WHERE language = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, languageCode);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    translations.put(
                            rs.getString("key"),
                            rs.getString("value")
                    );
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to load localization strings for language: " + languageCode,
                    e
            );
        }

        return translations;
    }
}