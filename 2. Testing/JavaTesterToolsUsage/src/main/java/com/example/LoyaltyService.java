package com.example;

import java.util.HashMap;
import java.util.Map;

public class LoyaltyService {
    private final Map<String, LoyaltyWallet> wallets;

    public LoyaltyService() {
        this.wallets = new HashMap<>();
    }

    public void processOrder(String userId, double orderAmount) {
        LoyaltyWallet wallet = wallets.computeIfAbsent(userId, LoyaltyWallet::new);

        // Рассчитываем баллы к начислению (10% от суммы, но не более 500)
        int pointsToCredit = (int) (orderAmount * 0.10);
        pointsToCredit = Math.min(pointsToCredit, 500);

        if (pointsToCredit > 0) {
            wallet.credit(pointsToCredit, "Начисление за заказ");
        }
    }

    public boolean makePurchaseWithPoints(String userId, int pointsToSpend) {
        LoyaltyWallet wallet = wallets.get(userId);
        if (wallet == null) {
            return false;
        }
        return wallet.debit(pointsToSpend, "Оплата заказа баллами");
    }

    public void addWallet(LoyaltyWallet wallet) {
        wallets.put(wallet.getUserId(), wallet);
    }

    public LoyaltyWallet getWallet(String userId) {
        return wallets.get(userId);
    }
}