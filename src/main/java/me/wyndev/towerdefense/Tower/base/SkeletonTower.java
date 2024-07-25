package me.wyndev.towerdefense.Tower.base;

import me.wyndev.towerdefense.Player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.Tower.Tower;
import me.wyndev.towerdefense.Tower.TowerType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

/**
 * A skeleton tower.
 */
public class SkeletonTower extends Tower {

    public SkeletonTower(@NotNull IngameTowerDefensePlayer playerWhoSpawned) {
        super(TowerType.SKELETON, playerWhoSpawned);
    }

    @Override
    public void initializeEntity(int towerLevel) {
        switch (towerLevel) {
            //TODO: levels 2 and 3
            default -> {
                setHelmet(ItemStack.builder(Material.LEATHER_HELMET).build());
                setItemInMainHand(ItemStack.builder(Material.BOW).build());
            }
        }
    }

    @Override
    public long getAttackSpeed(int towerLevel) {
        return 2000; //2 seconds
    }

    @Override
    public int getAttackRange(int towerLevel) {
        return 10; //10 blocks
    }
}
