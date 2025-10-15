import com.example.LoyaltyWallet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoyaltyWalletTest {
    private LoyaltyWallet wallet;

    @Before
    public void setUp() {
        wallet = new LoyaltyWallet("testUser");
    }

    @Test
    public void credit_ShouldIncreaseBalanceAndAddToHistory() {
        // Act
        wallet.credit(100, "Test credit");

        // Assert
        assertEquals(100, wallet.getBalance());
        assertTrue(wallet.getTransactionHistory().get(0).contains("CREDIT: +100"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void credit_WithZeroAmount_ShouldThrowException() {
        // Act & Assert
        wallet.credit(0, "Invalid credit");
    }

    @Test(expected = IllegalArgumentException.class)
    public void credit_WithNegativeAmount_ShouldThrowException() {
        // Act & Assert
        wallet.credit(-50, "Invalid credit");
    }

    @Test
    public void debit_WithSufficientFunds_ShouldDecreaseBalance() {
        // Arrange
        wallet.credit(200, "Initial credit");

        // Act
        boolean result = wallet.debit(150, "Test debit");

        // Assert
        assertTrue(result);
        assertEquals(50, wallet.getBalance());
        assertTrue(wallet.getTransactionHistory().get(1).contains("DEBIT: -150"));
    }

    @Test
    public void debit_WithInsufficientFunds_ShouldReturnFalse() {
        // Arrange
        wallet.credit(50, "Initial credit");

        // Act
        boolean result = wallet.debit(100, "Large debit");

        // Assert
        assertFalse(result);
        assertEquals(50, wallet.getBalance()); // Баланс не изменился
        assertTrue(wallet.getTransactionHistory().get(1).contains("FAILED DEBIT"));
    }
}