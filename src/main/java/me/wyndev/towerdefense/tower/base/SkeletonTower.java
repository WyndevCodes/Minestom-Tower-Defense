package me.wyndev.towerdefense.tower.base;

import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.tower.Tower;
import me.wyndev.towerdefense.tower.TowerType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

/**
 * A skeleton tower.
 */
public class SkeletonTower extends Tower {

    public SkeletonTower(@NotNull IngameTowerDefensePlayer playerWhoSpawned, int level) {
        super(TowerType.SKELETON, playerWhoSpawned, level);
        initializeEntity(level);
    }

    @Override
    public void initializeEntity(int towerLevel) {
        setItemInMainHand(ItemStack.builder(Material.BOW).build());
        switch (towerLevel) {
            //TODO: levels 2 and 3
            case 1: {
                setChestplate(ItemStack.builder(Material.LEATHER_CHESTPLATE).build());
                setLeggings(ItemStack.builder(Material.LEATHER_LEGGINGS).build());
                setBoots(ItemStack.builder(Material.LEATHER_BOOTS).build());
                break;
            }
            case 2: {
                setChestplate(ItemStack.builder(Material.IRON_CHESTPLATE).build());
                setLeggings(ItemStack.builder(Material.IRON_LEGGINGS).build());
                setBoots(ItemStack.builder(Material.IRON_BOOTS).build());
                break;
            }
            case 3: {
                setChestplate(ItemStack.builder(Material.GOLDEN_CHESTPLATE).build());
                setLeggings(ItemStack.builder(Material.GOLDEN_LEGGINGS).build());
                setBoots(ItemStack.builder(Material.GOLDEN_BOOTS).build());
                break;
            }
            case 4: {
                setChestplate(ItemStack.builder(Material.DIAMOND_CHESTPLATE).build());
                setLeggings(ItemStack.builder(Material.DIAMOND_LEGGINGS).build());
                setBoots(ItemStack.builder(Material.DIAMOND_BOOTS).build());
                break;
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
