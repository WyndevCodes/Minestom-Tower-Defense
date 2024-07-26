package me.wyndev.towerdefense;

import java.text.DecimalFormat;

public class Utils {
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Formats a number with commas. <br>
     * Example: 1234567 returns "1,234,567".
     *
     * @param number The number to format
     * @return The formatted number String
     * @author Wyndev
     */
    public static String formatWithCommas(double number) {
        String formattedNumber = new DecimalFormat("#,###.##").format(number);
        if (formattedNumber.endsWith("\\.0")) formattedNumber.replaceAll("\\.0", "");
        return formattedNumber;
    }
}
