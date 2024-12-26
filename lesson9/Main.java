package lesson9;

public class Main {
    public static String accountName = "MyAcc";
    public static int amount = 1700;

    public static void main(String[] args) throws InsuffitientMoney {
        System.out.println(deduct("MyAcc", 900));
    }

    public static int deduct(String accountToProcess, int amtToCacheOut) throws InsuffitientMoney {
        if (!accountName.equals(accountToProcess)) {
            throw new AccessDenied("Wrong Acc");
        }
        if (amount < amtToCacheOut) {
            throw new InsuffitientMoney("Is not enough money");
        }

        amount = amount - amtToCacheOut;
        return amount;
    }
}
