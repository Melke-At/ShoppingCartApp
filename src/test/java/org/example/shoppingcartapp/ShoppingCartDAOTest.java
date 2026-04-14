package org.example.shoppingcartapp;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartDAOTest {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @BeforeEach
    void setup() throws Exception {
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
    }

    // ---------------------------
    // TEST: saveCartRecord SUCCESS
    // ---------------------------
    @Test
    void testSaveCartRecord_success() throws Exception {

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
            when(stmt.getGeneratedKeys()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt(1)).thenReturn(123);

            ShoppingCartDAO dao = new ShoppingCartDAO();

            int id = dao.saveCartRecord(2, 50.0, "EN");

            assertEquals(123, id);
        }
    }

    // ---------------------------
    // TEST: saveCartRecord FAILURE
    // ---------------------------
    @Test
    void testSaveCartRecord_exception() throws Exception {

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenThrow(new SQLException());

            ShoppingCartDAO dao = new ShoppingCartDAO();

            assertThrows(RuntimeException.class,
                    () -> dao.saveCartRecord(1, 10.0, "EN"));
        }
    }

    // ---------------------------
    // TEST: saveCartItem SUCCESS
    // ---------------------------
    @Test
    void testSaveCartItem_success() throws Exception {

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);

            ShoppingCartDAO dao = new ShoppingCartDAO();

            assertDoesNotThrow(() ->
                    dao.saveCartItem(1, 1, 10.0, 2)
            );

            verify(stmt).setDouble(5, 20.0); // subtotal check
        }
    }

    // ---------------------------
    // TEST: getItemsForCart SUCCESS
    // ---------------------------
    @Test
    void testGetItemsForCart_success() throws Exception {

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);
            when(stmt.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);
            when(rs.getInt("item_number")).thenReturn(1);
            when(rs.getDouble("price")).thenReturn(10.0);
            when(rs.getInt("quantity")).thenReturn(2);
            when(rs.getDouble("subtotal")).thenReturn(20.0);

            ShoppingCartDAO dao = new ShoppingCartDAO();

            List<ShoppingCartDAO.CartItem> items = dao.getItemsForCart(1);

            assertEquals(1, items.size());
            assertEquals(20.0, items.get(0).getSubtotal());
        }
    }

    // ---------------------------
    // TEST: getItemsForCart EMPTY
    // ---------------------------
    @Test
    void testGetItemsForCart_empty() throws Exception {

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            ShoppingCartDAO dao = new ShoppingCartDAO();

            List<ShoppingCartDAO.CartItem> items = dao.getItemsForCart(1);

            assertTrue(items.isEmpty());
        }
    }

    // ---------------------------
    // TEST: CartItem getters
    // ---------------------------
    @Test
    void testCartItem_getters() {
        ShoppingCartDAO.CartItem item =
                new ShoppingCartDAO.CartItem(1, 10.0, 2, 20.0);

        assertEquals(1, item.getItemNumber());
        assertEquals(10.0, item.getPrice());
        assertEquals(2, item.getQuantity());
        assertEquals(20.0, item.getSubtotal());
    }
}