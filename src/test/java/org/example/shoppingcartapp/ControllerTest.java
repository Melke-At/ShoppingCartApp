package org.example.shoppingcartapp;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    private Controller controller;
    private CartService mockService;
    private LocalizationDAO mockDAO;

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setup() {

        // ✅ Mock dependencies
        mockService = mock(CartService.class);
        mockDAO = mock(LocalizationDAO.class);

        // Mock translations
        when(mockDAO.loadLanguage(anyString())).thenReturn(Map.of(
                "price", "Price",
                "quantity", "Quantity",
                "total", "Total",
                "selectLanguage", "Select Language",
                "enterItems", "Enter number of items",
                "generateFields", "Generate Fields",
                "calculate", "Calculate"
        ));

        // ✅ Inject mocks into controller
        controller = new Controller(mockService, mockDAO);

        // ✅ Mock UI components
        controller.languageSelector = new ComboBox<>();
        controller.labelSelectLanguage = new Label();
        controller.labelItems = new Label();
        controller.labelTotal = new Label();
        controller.generateFieldsButton = new Button();
        controller.calculateButton = new Button();
        controller.numberOfItemsField = new TextField();
        controller.itemsContainer = new VBox();
        controller.resultLabel = new Label();

        controller.initialize(); // important
    }

    // ---------------------------
    // TEST initialize
    // ---------------------------
    @Test
    void testInitialize() {
        assertEquals("en", controller.languageSelector.getValue());
        assertTrue(controller.languageSelector.getItems().contains("fi"));
    }

    // ---------------------------
    // TEST generateItemFields SUCCESS
    // ---------------------------
    @Test
    void testGenerateItemFields_success() {

        controller.numberOfItemsField.setText("2");

        controller.generateItemFields();

        assertEquals(2, controller.itemsContainer.getChildren().size());
    }

    // ---------------------------
    // TEST generateItemFields INVALID INPUT
    // ---------------------------
    @Test
    void testGenerateItemFields_invalidInput() {

        controller.numberOfItemsField.setText("abc");

        controller.generateItemFields();

        assertEquals("Invalid number", controller.resultLabel.getText());
    }

    // ---------------------------
    // TEST calculateTotal SUCCESS
    // ---------------------------
    @Test
    void testCalculateTotal_success() {

        // Create one item row
        HBox row = new HBox();

        TextField price = new TextField("10");
        TextField quantity = new TextField("2");

        row.getChildren().addAll(price, quantity);
        controller.itemsContainer.getChildren().add(row);

        controller.languageSelector.setValue("en");

        controller.calculateTotal();

        // Check result text
        assertTrue(controller.resultLabel.getText().contains("Total"));

        // Verify DB save called
        verify(mockService).saveCart(anyList(), eq("en"));
    }

    // ---------------------------
    // TEST calculateTotal INVALID INPUT
    // ---------------------------
    @Test
    void testCalculateTotal_invalidInput() {

        HBox row = new HBox();

        TextField price = new TextField("abc");
        TextField quantity = new TextField("2");

        row.getChildren().addAll(price, quantity);
        controller.itemsContainer.getChildren().add(row);

        controller.calculateTotal();

        assertEquals("Invalid input", controller.resultLabel.getText());

        // Ensure DB NOT called
        verify(mockService, never()).saveCart(anyList(), anyString());
    }
}