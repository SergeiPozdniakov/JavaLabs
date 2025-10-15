import com.example.LoyaltyService;
import com.example.LoyaltyWallet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoyaltyServiceIntegrationTest {
    private LoyaltyService service;

    @Before
    public void setUp() {
        service = new LoyaltyService();
    }

    @Test
    public void processOrder_ShouldCreditPointsToWallet() {
        // Arrange
        String userId = "user789";
        double orderAmount = 3000.0; // 10% = 300 баллов

        // Act
        service.processOrder(userId, orderAmount);
        LoyaltyWallet wallet = service.getWallet(userId);

        // Assert
        assertNotNull(wallet);
        assertEquals(300, wallet.getBalance());
        assertTrue(wallet.getTransactionHistory().get(0).contains("Начисление за заказ"));
    }

    @Test
    public void makePurchaseWithPoints_Success() {
        // Arrange
        String userId = "user123";
        service.processOrder(userId, 1000.0); // Начисляем 100 баллов

        // Act
        boolean result = service.makePurchaseWithPoints(userId, 50);
        LoyaltyWallet wallet = service.getWallet(userId);

        // Assert
        assertTrue(result);
        assertEquals(50, wallet.getBalance()); // 100 - 50 = 50
    }

    @Test
    public void makePurchaseWithPoints_InsufficientFunds() {
        // Arrange
        String userId = "user456";
        service.processOrder(userId, 500.0); // Начисляем 50 баллов

        // Act
        boolean result = service.makePurchaseWithPoints(userId, 100);
        LoyaltyWallet wallet = service.getWallet(userId);

        // Assert
        assertFalse(result);
        assertEquals(50, wallet.getBalance()); // Баланс не изменился
    }
}