package org.example.shoppingcartapp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {

    @FXML private ComboBox<String> languageBox;
    @FXML private Label labelItems, labelTotal;
    @FXML private TextField numItemsField;
    @FXML private VBox itemsContainer;
    @FXML private Button generateFieldsButton;
    @FXML private Button calculateButton;
    @FXML private Label labelSelectLanguage;


    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        languageBox.getItems().addAll("English", "Finnish", "Swedish", "Japanese", "Arabic");
        languageBox.setValue("English");
        loadLanguage(new Locale("en", "US"));
    }

    private void loadLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n.MessagesBundle", locale);

        labelSelectLanguage.setText(bundle.getString("selectLanguage"));
        labelItems.setText(bundle.getString("enterItems"));
        labelTotal.setText(bundle.getString("total"));
        generateFieldsButton.setText(bundle.getString("generateFields"));
        calculateButton.setText(bundle.getString("calculate"));
    }


    @FXML
    private void changeLanguage() {
        switch (languageBox.getValue()) {
            case "Finnish" -> loadLanguage(new Locale("fi", "FI"));
            case "Swedish" -> loadLanguage(new Locale("sv", "SE"));
            case "Japanese" -> loadLanguage(new Locale("ja", "JP"));
            case "Arabic" -> loadLanguage(new Locale("ar", "AR"));
            default -> loadLanguage(new Locale("en", "US"));
        }
    }

    @FXML
    private void generateFields() {
        itemsContainer.getChildren().clear();
        int count = Integer.parseInt(numItemsField.getText());

        for (int i = 0; i < count; i++) {
            TextField price = new TextField();
            price.setPromptText(bundle.getString("price"));

            TextField quantity = new TextField();
            quantity.setPromptText(bundle.getString("quantity"));

            itemsContainer.getChildren().add(new HBox(10, price, quantity));
        }
    }

    @FXML
    private void calculateTotal() {
        double total = 0;

        for (Node node : itemsContainer.getChildren()) {
            HBox row = (HBox) node;
            TextField priceField = (TextField) row.getChildren().get(0);
            TextField qtyField = (TextField) row.getChildren().get(1);

            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            total += price * qty;
        }

        labelTotal.setText(bundle.getString("total") + ": " + total);
    }
}

