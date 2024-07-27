package me.wyndev.towerdefense.game.chestui;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class UICommon {
    public static final ItemStack border = ItemStack.of(Material.BLUE_STAINED_GLASS_PANE).withCustomName(Component.empty());
    public static final ItemStack delimiter = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE).withCustomName(Component.empty());

    public static final int[] ChestBorder = new int[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1,
    };

    public static final int[] HopperDelimiter = new int[]{
            0, 1, 0, 1, 0
    };


}
