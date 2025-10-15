package ToolsTests;

import com.example.LoyaltyService;
import com.example.LoyaltyWallet;
import io.cucumber.java.Before;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;

import static org.junit.Assert.*;

public class LoyaltySteps {

    private LoyaltyService loyaltyService;
    private LoyaltyWallet currentWallet;
    private boolean lastOperationResult;

    @Before
    public void setUp() {
        loyaltyService = new LoyaltyService();
    }

    @Дано("у пользователя {string} есть кошелек с балансом {int} баллов")
    public void создать_кошелек_с_балансом(String userId, int initialBalance) {
        currentWallet = new LoyaltyWallet(userId);
        if (initialBalance > 0) {
            currentWallet.credit(initialBalance, "Начальный баланс");
        }
        loyaltyService.addWallet(currentWallet);
    }

    @Когда("пользователь {string} совершает заказ на сумму {int} рублей")
    public void пользователь_совершает_заказ(String userId, int orderAmount) {
        loyaltyService.processOrder(userId, orderAmount);
        currentWallet = loyaltyService.getWallet(userId);
    }

    @Когда("пользователь {string} пытается списать {int} баллов для оплаты заказа")
    public void попытка_списания_баллов(String userId, int amount) {
        lastOperationResult = loyaltyService.makePurchaseWithPoints(userId, amount);
        currentWallet = loyaltyService.getWallet(userId);
    }

    @Тогда("его баланс должен стать {int} баллов")
    public void проверить_баланс(int expectedBalance) {
        assertNotNull("Кошелек не создан", currentWallet);
        assertEquals("Ожидаемый баланс: " + expectedBalance + ", фактический: " + currentWallet.getBalance(),
                expectedBalance, currentWallet.getBalance());
    }

    @Тогда("баланс его кошелька должен стать {int} баллов")
    public void баланс_его_кошелька_должен_стать_баллов(int expectedBalance) {
        проверить_баланс(expectedBalance);
    }

    @Тогда("операция должна быть успешной")
    public void операция_должна_быть_успешной() {
        assertTrue("Операция должна быть успешной", lastOperationResult);
    }

    @Тогда("операция должна быть отклонена")
    public void операция_должна_быть_отклонена() {
        assertFalse("Операция должна быть отклонена", lastOperationResult);
    }

    @И("баланс его кошелька должен остаться {int} баллов")
    public void баланс_должен_остаться(int expectedBalance) {
        проверить_баланс(expectedBalance);
    }

    @И("в истории операций должна быть запись о начислении")
    public void проверить_запись_о_начислении() {
        assertNotNull("Кошелек не создан", currentWallet);
        java.util.List<String> history = currentWallet.getTransactionHistory();
        boolean found = history.stream()
                .anyMatch(record -> record.contains("CREDIT:") && record.contains("Начисление"));
        assertTrue("В истории должна быть запись о начислении\nАктуальная история: " + history, found);
    }

    @И("в истории операций должна быть запись о неудачной попытке списания")
    public void проверить_запись_о_неудачном_списании() {
        assertNotNull("Кошелек не создан", currentWallet);
        java.util.List<String> history = currentWallet.getTransactionHistory();
        boolean found = history.stream()
                .anyMatch(record -> record.contains("FAILED DEBIT:"));
        assertTrue("В истории должна быть запись о неудачном списании\nАктуальная история: " + history, found);
    }

    @И("в истории операций должна быть запись о успешном списании")
    public void проверить_запись_о_успешном_списании() {
        assertNotNull("Кошелек не создан", currentWallet);
        java.util.List<String> history = currentWallet.getTransactionHistory();
        boolean found = history.stream()
                .anyMatch(record -> record.contains("DEBIT:") && !record.contains("FAILED"));
        assertTrue("В истории должна быть запись об успешном списании\nАктуальная история: " + history, found);
    }
}