package me.wyndev.towerdefense;

import net.kyori.adventure.text.format.TextColor;

/**
 * Our own implementation of the Bukkit ChatColor class
 * for text colors we may want to use.
 */
public enum ChatColor {
    BLACK(0, 0, 0),
    DARK_BLUE(0, 0, 170),
    GOLD(255, 170, 0),
    GRAY(170, 170, 170),
    GREEN(85, 255, 85),
    RED(255, 85, 85),
    LIGHT_PURPLE(255, 85, 255),
    WHITE(255, 255, 255),
    YELLOW(255, 255, 85),
    ;

    private final int r;
    private final int g;
    private final int b;

    ChatColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * @return Converts this {@link ChatColor} to a {@link TextColor}.
     */
    public TextColor toColor() {
        return TextColor.color(r, g, b);
    }
}
