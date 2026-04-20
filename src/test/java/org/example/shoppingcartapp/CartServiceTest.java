package org.example.shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private static final String LANGUAGE = "en";
    private static final String DB_ERROR = "DB error";

    // TEST 1: Default constructor initializes DAO successfully
    @Test
    void shouldInitializeDao_whenDefaultConstructorIsUsed() {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            Connection connectionMock = mock(Connection.class);
            mocked.when(DatabaseConnection::getConnection)
                    .thenReturn(connectionMock);

            CartService service = new CartService();

            assertNotNull(service);
        }
    }

    // TEST 2: Default constructor wraps DAO init failure
    @Test
    void shouldThrowCartServiceException_whenDaoInitializationFails() {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException(DB_ERROR));

            assertThrows(CartServiceException.class, CartService::new);
        }
    }

    // TEST 3: saveCart success path
    @Test
    void shouldSaveCartSuccessfully_whenValidItemsProvided() throws Exception {
        ShoppingCartDAO daoMock = mock(ShoppingCartDAO.class);

        when(daoMock.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(10);

        CartService service = new CartService(daoMock);

        List<CartItemDTO> items = createSampleCartItems();

        int result = service.saveCart(items, LANGUAGE);

        assertEquals(10, result);

        verify(daoMock).saveCartRecord(2, 25.0, LANGUAGE);
        verify(daoMock).saveCartItem(10, 1, 10.0, 2);
        verify(daoMock).saveCartItem(10, 2, 5.0, 1);
    }

    // TEST 4: saveCart wraps DAO exception
    @Test
    void shouldThrowCartServiceException_whenDaoThrowsException() throws Exception {
        ShoppingCartDAO daoMock = mock(ShoppingCartDAO.class);

        when(daoMock.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenThrow(new ShoppingCartException(DB_ERROR, null));

        CartService service = new CartService(daoMock);

        List<CartItemDTO> items = List.of(new CartItemDTO(10.0, 2));

        assertThrows(CartServiceException.class,
                () -> service.saveCart(items, LANGUAGE));
    }

    // Helper method to reduce duplication and improve readability
    private List<CartItemDTO> createSampleCartItems() {
        return List.of(
                new CartItemDTO(10.0, 2),
                new CartItemDTO(5.0, 1)
        );
    }
}