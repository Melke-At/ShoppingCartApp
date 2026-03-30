package org.example.shoppingcartapp;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorTest {

    CartCalculator service = new CartCalculator();

    @Test
    void testItemTotal() {
        assertEquals(20.0, service.calculateItemTotal(10, 2));
    }

    @Test
    void testCartTotal() {
        assertEquals(60.0, service.calculateCartTotal(List.of(10.0, 20.0, 30.0)));
    }
}