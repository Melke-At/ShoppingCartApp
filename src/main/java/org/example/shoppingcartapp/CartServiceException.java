package org.example.shoppingcartapp;

public class CartServiceException extends RuntimeException {
    public CartServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
