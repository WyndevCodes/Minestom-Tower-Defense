package me.wyndev.towerdefense;

public class Utils {
    public static void sleep(int time) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
