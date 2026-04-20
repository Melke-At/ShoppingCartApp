package org.example.shoppingcartapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartDAOTest {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;
    private ShoppingCartDAO dao;

    @BeforeEach
    void setup() {
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        dao = new ShoppingCartDAO(conn);
    }

    // ---------------------------
    // TEST saveCartRecord SUCCESS
    // ---------------------------
    @Test
    void testSaveCartRecord_success() throws Exception {

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(stmt);

        when(stmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(100);

        int result = dao.saveCartRecord(3, 50.0, "EN");

        assertEquals(100, result);

        verify(stmt).setInt(1, 3);
        verify(stmt).setDouble(2, 50.0);
        verify(stmt).setString(3, "EN");
        verify(stmt).executeUpdate();
    }

    // ---------------------------
    // TEST saveCartRecord NO GENERATED KEY
    // ---------------------------
    @Test
    void testSaveCartRecord_noGeneratedKey() throws Exception {

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(stmt);

        when(stmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        int result = dao.saveCartRecord(3, 50.0, "EN");

        assertEquals(-1, result);
    }

    // ---------------------------
    // TEST saveCartItem SUCCESS
    // ---------------------------
    @Test
    void testSaveCartItem_success() throws Exception {

        when(conn.prepareStatement(anyString())).thenReturn(stmt);

        dao.saveCartItem(1, 2, 10.0, 3);

        verify(stmt).setInt(1, 1);
        verify(stmt).setInt(2, 2);
        verify(stmt).setDouble(3, 10.0);
        verify(stmt).setInt(4, 3);
        verify(stmt).setDouble(5, 30.0);
        verify(stmt).executeUpdate();
    }

    // ---------------------------
    // TEST getItemsForCart SUCCESS
    // ---------------------------
    @Test
    void testGetItemsForCart_success() throws Exception {

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getInt("item_number")).thenReturn(1, 2);
        when(rs.getDouble("price")).thenReturn(10.0, 20.0);
        when(rs.getInt("quantity")).thenReturn(2, 1);
        when(rs.getDouble("subtotal")).thenReturn(20.0, 20.0);

        List<ShoppingCartDAO.CartItem> items = dao.getItemsForCart(1);

        assertEquals(2, items.size());
    }

    // ---------------------------
    // TEST getItemsForCart EMPTY
    // ---------------------------
    @Test
    void testGetItemsForCart_empty() throws Exception {

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        List<ShoppingCartDAO.CartItem> items = dao.getItemsForCart(1);

        assertTrue(items.isEmpty());
    }

    // ---------------------------
    // TEST exception handling
    // ---------------------------
    @Test
    void testSaveCartRecord_exception() throws Exception {

        when(conn.prepareStatement(anyString(), anyInt()))
                .thenThrow(new SQLException("DB error"));

        assertThrows(ShoppingCartException.class, () ->
                dao.saveCartRecord(1, 10.0, "EN")
        );
    }

    // ---------------------------
    // TEST constructor exception
    // ---------------------------
    @Test
    void testConstructor_exception() {

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException("DB error"));

            assertThrows(ShoppingCartException.class, ShoppingCartDAO::new);
        }
    }

    // ---------------------------
    // TEST CartItem getters
    // ---------------------------
    @Test
    void testCartItemGetters() {

        ShoppingCartDAO.CartItem item =
                new ShoppingCartDAO.CartItem(1, 10.0, 2, 20.0);

        assertEquals(1, item.getItemNumber());
        assertEquals(10.0, item.getPrice());
        assertEquals(2, item.getQuantity());
        assertEquals(20.0, item.getSubtotal());
    }
}
