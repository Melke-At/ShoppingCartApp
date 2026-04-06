package org.example.shoppingcartapp;

import java.util.List;

public class CartService {

    private final ShoppingCartDAO cartDAO;

    public CartService() {
        this.cartDAO = new ShoppingCartDAO();
    }

    /**
     * Saves a full shopping cart session:
     * 1. Creates a cart_record entry
     * 2. Saves each cart item linked to that record
     *
     * @param items    List of CartItemDTO objects (price, quantity)
     * @param language Selected UI language (e.g., "en", "fi", "sv", "ja", "ar")
     * @return the generated cart_record_id
     */
    public int saveCart(List<CartItemDTO> items, String language) {

        int totalItems = items.size();
        double totalCost = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        // Save main cart record
        int cartRecordId = cartDAO.saveCartRecord(totalItems, totalCost, language);

        // Save each item
        int itemNumber = 1;
        for (CartItemDTO item : items) {
            cartDAO.saveCartItem(
                    cartRecordId,
                    itemNumber++,
                    item.getPrice(),
                    item.getQuantity()
            );
        }

        return cartRecordId;
    }
}
