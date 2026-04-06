package org.example.shoppingcartapp;

public class CartItemDTO {

    private final double price;
    private final int quantity;

    public CartItemDTO(double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
