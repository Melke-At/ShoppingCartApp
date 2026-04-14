package org.example.shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Test
    void testSaveCart_success() throws Exception {

        // ✅ Arrange
        CartService service = new CartService();

        // Mock DAO
        ShoppingCartDAO mockDAO = mock(ShoppingCartDAO.class);

        // Inject mock into private field using reflection
        Field field = CartService.class.getDeclaredField("cartDAO");
        field.setAccessible(true);
        field.set(service, mockDAO);

        // Test data
        CartItemDTO item1 = new CartItemDTO(10.0, 2); // 20
        CartItemDTO item2 = new CartItemDTO(5.0, 3);  // 15

        List<CartItemDTO> items = List.of(item1, item2);

        // Mock DAO behavior
        when(mockDAO.saveCartRecord(2, 35.0, "en")).thenReturn(100);

        // ✅ Act
        int result = service.saveCart(items, "en");

        // ✅ Assert
        assertEquals(100, result);

        // Verify main record saved
        verify(mockDAO, times(1))
                .saveCartRecord(2, 35.0, "en");

        // Verify items saved
        verify(mockDAO, times(1))
                .saveCartItem(100, 1, 10.0, 2);

        verify(mockDAO, times(1))
                .saveCartItem(100, 2, 5.0, 3);
    }
}