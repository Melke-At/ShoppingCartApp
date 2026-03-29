import org.example.CartService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    CartService service = new CartService();

    @Test
    void testItemTotal() {
        assertEquals(20.0, service.calculateItemTotal(10, 2));
    }

    @Test
    void testCartTotal() {
        List<Double> items = List.of(10.0, 20.0, 30.0);
        assertEquals(60.0, service.calculateCartTotal(items));
    }
}