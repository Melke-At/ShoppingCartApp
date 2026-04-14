package org.example.shoppingcartapp;

import java.util.List;

public class CartService {

    private final ShoppingCartDAO cartDAO;

    // Default constructor used by JavaFX
    public CartService() {
        this(createDAO());
    }

    // Helper method to wrap checked exception (Sonar-friendly)
    private static ShoppingCartDAO createDAO() {
        try {
            return new ShoppingCartDAO();
        } catch (ShoppingCartException e) {
            throw new CartServiceException("Failed to initialize ShoppingCartDAO", e);
        }
    }

    // Constructor for testing (dependency injection)
    public CartService(ShoppingCartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    /**
     * Saves a full shopping cart session.
     */
    public int saveCart(List<CartItemDTO> items, String language) {
        try {
            int totalItems = items.size();
            double totalCost = items.stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            int cartRecordId = cartDAO.saveCartRecord(totalItems, totalCost, language);

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

        } catch (ShoppingCartException e) {
            throw new CartServiceException("Failed to save cart", e);
        }
    }
}
