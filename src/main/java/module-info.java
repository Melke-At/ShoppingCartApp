module shopping.cart.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.shoppingcartapp to javafx.fxml;
    exports org.example.shoppingcartapp;
}
