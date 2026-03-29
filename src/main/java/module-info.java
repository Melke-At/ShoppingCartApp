module org.example.shoppingcartapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.shoppingcartapp to javafx.fxml;
    exports org.example.shoppingcartapp;
}