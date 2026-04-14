package org.example.shoppingcartapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemDTOTest {

    @Test
    void testConstructorAndGetters() {
        double price = 19.99;
        int quantity = 3;

        CartItemDTO item = new CartItemDTO(price, quantity);

        assertEquals(price, item.getPrice(), "Price should match the constructor value");
        assertEquals(quantity, item.getQuantity(), "Quantity should match the constructor value");
    }

    @Test
    void testZeroValues() {
        CartItemDTO item = new CartItemDTO(0.0, 0);
        assertEquals(0.0, item.getPrice(), "Price should be zero");
        assertEquals(0, item.getQuantity(), "Quantity should be zero");
    }

    @Test
    void testNegativeValues() {
        CartItemDTO item = new CartItemDTO(-5.5, -2);
        assertEquals(-5.5, item.getPrice(), "Negative price should be stored correctly");
        assertEquals(-2, item.getQuantity(), "Negative quantity should be stored correctly");
    }
}
