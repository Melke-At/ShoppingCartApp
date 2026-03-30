module shopping.cart.app {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.shoppingcartapp to javafx.fxml;
    exports org.example.shoppingcartapp;
}
