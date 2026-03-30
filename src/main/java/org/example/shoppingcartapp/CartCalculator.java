package org.example.shoppingcartapp;

import java.util.List;

public class CartCalculator {

    public double calculateItemTotal(double price, int quantity) {
        return price * quantity;
    }

    public double calculateCartTotal(List<Double> items) {
        return items.stream().mapToDouble(Double::doubleValue).sum();
    }
}
