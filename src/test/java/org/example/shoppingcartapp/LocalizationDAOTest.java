package org.example.shoppingcartapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalizationDAOTest {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;
    private LocalizationDAO dao;

    @BeforeEach
    void setup() {

        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        // ✅ NOW using injected constructor (NO real DB)
        dao = new LocalizationDAO(conn);
    }

    // ---------------------------
    // SUCCESS CASE
    // ---------------------------
    @Test
    void testLoadLanguage_success() throws Exception {

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("key")).thenReturn("total", "price");
        when(rs.getString("value")).thenReturn("Total", "Price");

        Map<String, String> result = dao.loadLanguage("en");

        assertEquals(2, result.size());
        assertEquals("Total", result.get("total"));
        assertEquals("Price", result.get("price"));

        verify(stmt).setString(1, "en");
    }

    // ---------------------------
    // EMPTY RESULT
    // ---------------------------
    @Test
    void testLoadLanguage_empty() throws Exception {

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Map<String, String> result = dao.loadLanguage("en");

        assertTrue(result.isEmpty());
    }

    // ---------------------------
    // EXCEPTION CASE
    // ---------------------------
    @Test
    void testLoadLanguage_exception() throws Exception {

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("DB error"));

        assertThrows(IllegalStateException.class, () ->
                dao.loadLanguage("en")
        );
    }
}