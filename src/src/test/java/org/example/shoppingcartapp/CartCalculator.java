package org.example.shoppingcartapp;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorTest {

    CartCalculator calculator = new CartCalculator();

    @Test
    void testCalculateItemTotal() {
        double result = calculator.calculateItemTotal(10.0, 3);
        assertEquals(30.0, result, 0.001);
    }

    @Test
    void testCalculateItemTotalWithZeroQuantity() {
        double result = calculator.calculateItemTotal(10.0, 0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testCalculateCartTotal() {
        List<Double> items = List.of(10.0, 20.0, 30.0);
        double result = calculator.calculateCartTotal(items);
        assertEquals(60.0, result, 0.001);
    }

    @Test
    void testCalculateCartTotalEmptyList() {
        List<Double> items = List.of();
        double result = calculator.calculateCartTotal(items);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testCalculateCartTotalSingleItem() {
        List<Double> items = List.of(99.99);
        double result = calculator.calculateCartTotal(items);
        assertEquals(99.99, result, 0.001);
    }
}