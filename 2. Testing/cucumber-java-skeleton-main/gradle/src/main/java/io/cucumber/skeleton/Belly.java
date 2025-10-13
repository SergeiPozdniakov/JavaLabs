package io.cucumber.skeleton;

public class Belly {
    private int cukes;
    private int hoursWaited;
    private boolean hasSoda;
    private int digestionRate = 10; // огурцов в час

    public void eat(int cukes) {
        this.cukes += cukes;  // ← ИСПРАВЛЕНО: аккумулируем огурцы
        System.out.println("Ate " + cukes + " cukes. Total: " + this.cukes);
    }

    public void drinkSoda() {
        this.hasSoda = true;
        this.digestionRate = 15; // сода ускоряет переваривание
        System.out.println("Drank soda! Digestion rate increased.");
    }

    public void wait(int hours) {
        this.hoursWaited = hours;
        int digested = hours * digestionRate;
        this.cukes = Math.max(0, this.cukes - digested);
        System.out.println("Waited " + hours + " hours. Digested: " + digested + " cukes. Remaining: " + this.cukes);
    }

    public boolean shouldGrowl() {
        boolean result = cukes < 20 || hoursWaited >= 1;
        System.out.println("Should growl? " + result + " (cukes: " + cukes + ", hours: " + hoursWaited + ")");
        return result;
    }

    public boolean isSatisfied() {
        // ИСПРАВЛЕНО: удовлетворены если много огурцов И не урчит
        boolean result = cukes >= 25;
        System.out.println("Is satisfied? " + result + " (cukes: " + cukes + ")");
        return result;
    }

    public String getMood() {
        if (cukes > 50) return "HAPPY";
        if (cukes > 20) return "CONTENT";
        if (shouldGrowl()) return "HUNGRY";
        return "NEUTRAL";
    }

    // Геттеры для проверок
    public int getCukes() { return cukes; }
    public int getHoursWaited() { return hoursWaited; }
    public boolean hasSoda() { return hasSoda; }
}