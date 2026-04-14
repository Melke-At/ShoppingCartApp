package org.example.shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    // TEST 1: Default constructor initializes DAO successfully
    @Test
    void testDefaultConstructor_initializesDAO() throws Exception {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection)
                    .thenReturn(mock(java.sql.Connection.class));

            CartService service = new CartService();

            assertNotNull(service);
        }
    }

    // TEST 2: Default constructor wraps DAO init failure
    @Test
    void testDefaultConstructor_throwsCartServiceException() throws Exception {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException("DB error"));

            assertThrows(CartServiceException.class, CartService::new);
        }
    }

    // TEST 3: saveCart success path
    @Test
    void testSaveCart_success() throws Exception {
        ShoppingCartDAO dao = mock(ShoppingCartDAO.class);

        when(dao.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(10);

        CartService service = new CartService(dao);

        List<CartItemDTO> items = List.of(
                new CartItemDTO(10.0, 2),
                new CartItemDTO(5.0, 1)
        );

        int result = service.saveCart(items, "en");

        assertEquals(10, result);

        verify(dao).saveCartRecord(2, 25.0, "en");
        verify(dao).saveCartItem(10, 1, 10.0, 2);
        verify(dao).saveCartItem(10, 2, 5.0, 1);
    }

    // TEST 4: saveCart wraps DAO exception
    @Test
    void testSaveCart_throwsCartServiceException() throws Exception {
        ShoppingCartDAO dao = mock(ShoppingCartDAO.class);

        when(dao.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenThrow(new ShoppingCartException("DB error", null));

        CartService service = new CartService(dao);

        List<CartItemDTO> items = List.of(new CartItemDTO(10.0, 2));

        assertThrows(CartServiceException.class, () ->
                service.saveCart(items, "en")
        );
    }
}
