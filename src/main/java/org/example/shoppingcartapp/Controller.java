package org.example.shoppingcartapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    @FXML
    ComboBox<String> languageSelector;
    @FXML
    Label labelSelectLanguage;
    @FXML
    Label labelItems;
    @FXML
    Label labelTotal;
    @FXML
    Button generateFieldsButton;
    @FXML
    Button calculateButton;
    @FXML
    TextField numberOfItemsField;
    @FXML
    VBox itemsContainer;
    @FXML
    Label resultLabel;

    private final CartCalculator calculator = new CartCalculator();

    // ❗ Removed "final" and direct initialization
    private CartService cartService;
    private LocalizationDAO localizationDAO;

    Map<String, String> translations;

    // ✅ Default constructor (used by JavaFX)
    public Controller() {
        this.cartService = new CartService();
        this.localizationDAO = new LocalizationDAO();
    }

    // ✅ Constructor for testing (dependency injection)
    public Controller(CartService cartService, LocalizationDAO localizationDAO) {
        this.cartService = cartService;
        this.localizationDAO = localizationDAO;
    }

    @FXML
    public void initialize() {
        languageSelector.getItems().addAll("en", "fi", "sv", "ja", "ar");
        languageSelector.setValue("en");
        loadLanguage("en");
    }

    @FXML
    private void onLanguageChanged() {
        String selected = languageSelector.getValue();
        loadLanguage(selected);
    }

    private void loadLanguage(String lang) {
        translations = localizationDAO.loadLanguage(lang);

        labelSelectLanguage.setText(translations.getOrDefault("selectLanguage", "Select Language"));
        labelItems.setText(translations.getOrDefault("enterItems", "Enter number of items"));
        labelTotal.setText(translations.getOrDefault("total", "Total"));
        generateFieldsButton.setText(translations.getOrDefault("generateFields", "Generate Fields"));
        calculateButton.setText(translations.getOrDefault("calculate", "Calculate"));
    }

    @FXML
    void generateItemFields() {
        itemsContainer.getChildren().clear();

        int count;
        try {
            count = Integer.parseInt(numberOfItemsField.getText());
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid number");
            return;
        }

        for (int i = 0; i < count; i++) {
            HBox row = new HBox(10);

            TextField priceField = new TextField();
            priceField.setPromptText(translations.getOrDefault("price", "Price"));

            TextField quantityField = new TextField();
            quantityField.setPromptText(translations.getOrDefault("quantity", "Quantity"));

            row.getChildren().addAll(priceField, quantityField);
            itemsContainer.getChildren().add(row);
        }
    }

    @FXML
    void calculateTotal() {
        List<CartItemDTO> items = new ArrayList<>();

        for (var node : itemsContainer.getChildren()) {
            if (node instanceof HBox row) {
                TextField priceField = (TextField) row.getChildren().get(0);
                TextField quantityField = (TextField) row.getChildren().get(1);

                try {
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());

                    items.add(new CartItemDTO(price, quantity));

                } catch (NumberFormatException e) {
                    resultLabel.setText("Invalid input");
                    return;
                }
            }
        }

        // Calculate total
        double total = items.stream()
                .mapToDouble(i -> calculator.calculateItemTotal(i.getPrice(), i.getQuantity()))
                .sum();

        resultLabel.setText(translations.getOrDefault("total", "Total") + ": " + total);

        // Save to DB (mocked in tests)
        String lang = languageSelector.getValue();
        cartService.saveCart(items, lang);
    }
}